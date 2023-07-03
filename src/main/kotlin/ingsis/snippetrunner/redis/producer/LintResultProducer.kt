package ingsis.snippetrunner.redis.producer

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import snippet.events.LintResultEvent
import spring.mvc.redis.streams.RedisStreamProducer
import java.util.*

@Component
class LintResultProducer @Autowired constructor(
    @Value("\${stream.key}") streamKey: String,
    redis: RedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {

    suspend fun publishEvent(snippetId: UUID, status: String) {
        println("Publishing on stream: $streamKey")
        val product = LintResultEvent(snippetId.toString(), status)
        emit(product)
    }

    suspend fun publishEvent(event: LintResultEvent) {
        println("Publishing on stream: $streamKey")
        emit(event)
    }

}