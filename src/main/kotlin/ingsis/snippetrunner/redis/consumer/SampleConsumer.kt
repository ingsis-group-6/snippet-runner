package ingsis.snippetrunner.redis.consumer

import ingsis.snippetrunner.model.event.LintResultEvent
import org.austral.ingsis.`class`.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class SampleConsumer @Autowired constructor(
    redis: ReactiveRedisTemplate<String, String>,
    @Value("\${stream.key}") streamKey: String,
    @Value("\${groups.lint}") groupId: String
) : RedisStreamConsumer<LintResultEvent>(streamKey, groupId, redis) {
    override fun onMessage(record: ObjectRecord<String, LintResultEvent>) {
        println("Received event of snippet ${record.value.lintedSnippetId} with status ${record.value.status}")
    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintResultEvent>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(100)) // Set poll rate
            .targetType(LintResultEvent::class.java) // Set type to de-serialize record
            .build();
    }

}