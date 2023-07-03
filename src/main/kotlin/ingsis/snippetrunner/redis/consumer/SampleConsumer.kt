package ingsis.snippetrunner.redis.consumer


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import snippet.events.LintResultEvent
import spring.mvc.redis.streams.RedisStreamConsumer
import java.time.Duration

@Component
class SampleConsumer @Autowired constructor(
    redis: RedisTemplate<String, String>,
    @Value("\${stream.key}") streamKey: String,
    @Value("\${groups.lint}") groupId: String
) : RedisStreamConsumer<LintResultEvent>(streamKey, groupId, redis) {

    init {
        subscription()
    }

    override fun onMessage(record: ObjectRecord<String, LintResultEvent>) {
        println("Received event of snippet ${record.value.lintedSnippetId} with status ${record.value.status}")
    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintResultEvent>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
            .targetType(LintResultEvent::class.java) // Set type to de-serialize record
            .build();
    }

}