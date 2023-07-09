package ingsis.snippetrunner.redis.consumer


import ingsis.snippetrunner.redis.producer.LintResultProducer
import ingsis.snippetrunner.service.PrintscriptRunnerService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import snippet.events.lint.LintRequestEvent
import snippet.events.lint.LintResultEvent
import snippet.events.lint.LintResultStatus
import spring.mvc.redis.streams.RedisStreamConsumer
import java.time.Duration

@Component
class LintRequestConsumer @Autowired constructor(
    redis: RedisTemplate<String, String>,
    @Value("\${stream.request_key}") streamKey: String,
    @Value("\${groups.lint}") groupId: String,
    @Autowired private val runnerService: PrintscriptRunnerService,
    @Autowired private val lintResultProducer: LintResultProducer
) : RedisStreamConsumer<LintRequestEvent>(streamKey, groupId, redis) {

    init {
        subscription()
    }

    override fun onMessage(record: ObjectRecord<String, LintRequestEvent>) {
        println("Received event of snippet ${record.value.snippetId} with content ${record.value.snippetContent}")
        val result = runnerService.lint(record.value.snippetContent, "1.0")
        val event = LintResultEvent(record.value.snippetId, if(result.isEmpty()) LintResultStatus.COMPLIANT else LintResultStatus.NON_COMPLIANT)
        GlobalScope.launch {
            lintResultProducer.publishEvent(event)
        }


    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintRequestEvent>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(1000000)) // Set poll rate
            .targetType(LintRequestEvent::class.java) // Set type to de-serialize record
            .build();
    }

}