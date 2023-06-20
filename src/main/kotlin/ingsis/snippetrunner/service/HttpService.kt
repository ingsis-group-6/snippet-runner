package ingsis.snippetrunner.service

import ingsis.snippetrunner.model.dto.SnippetDTO
import io.github.cdimascio.dotenv.Dotenv
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class HttpService {
    @Throws(HttpClientErrorException::class)
    fun getSnippetCodeFromManager(token: String, snippetId: UUID): SnippetDTO {
        val env = Dotenv.load()
        val url = env["MANAGER_URI"] + "/snippet/" + snippetId.toString()
        val template = RestTemplate()
        val headers = org.springframework.http.HttpHeaders()
        headers.set("Authorization", token)
        val requestEntity = HttpEntity<Void>(headers)

        try {
            val response: ResponseEntity<SnippetDTO> = template.exchange(url, HttpMethod.GET, requestEntity, SnippetDTO::class.java)
            return response.body!!
        } catch (ex: HttpClientErrorException) {
            throw ex // Optionally, rethrow the exception or return a default value
        }
    }
}