package ingsis.snippetrunner.redis.producer

import kotlinx.coroutines.reactor.awaitSingle
import org.austral.ingsis.`class`.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import snippet.events.lint.LintResultEvent
import snippet.events.lint.LintResultStatus
import java.util.*

@Component
class LintResultProducer @Autowired constructor(
    @Value("\${stream.result_key}") streamKey: String,
    redis: ReactiveRedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {


    suspend fun publishEvent(event: LintResultEvent) {
        println("Publishing on stream: $streamKey")
        emit(event).awaitSingle()
    }

}