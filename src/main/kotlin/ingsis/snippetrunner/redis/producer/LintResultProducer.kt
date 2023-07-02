package ingsis.snippetrunner.redis.producer

import ingsis.snippetrunner.model.event.LintResultEvent
import kotlinx.coroutines.reactor.awaitSingle
import org.aspectj.weaver.Lint
import org.austral.ingsis.`class`.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class LintResultProducer @Autowired constructor(
    @Value("\${stream.key}") streamKey: String,
    redis: ReactiveRedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {

    suspend fun publishEvent(snippetId: UUID, status: String) {
        println("Publishing on stream: $streamKey")
        val product = LintResultEvent(snippetId, status)
        emit(product).awaitSingle()
    }

    suspend fun publishEvent(event: LintResultEvent) {
        println("Publishing on stream: $streamKey")
        emit(event).awaitSingle()
    }

}