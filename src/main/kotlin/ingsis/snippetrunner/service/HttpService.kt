package ingsis.snippetrunner.service

import ingsis.snippetrunner.model.dto.SnippetDTO
import ingsis.snippetrunner.model.dto.TestDTO
import ingsis.snippetrunner.model.dto.rules.FormatterRulesDTO
import ingsis.snippetrunner.model.dto.rules.LinterRulesDTO
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
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

        val url = System.getenv("MANAGER_URI") + "/snippet/" + snippetId.toString()
        val template = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", token)
        val requestEntity = HttpEntity<Void>(headers)

        val response: ResponseEntity<SnippetDTO> = template.exchange(url, HttpMethod.GET, requestEntity, SnippetDTO::class.java)
        return response.body!!

    }

    @Throws(HttpClientErrorException::class)
    fun updateSnippetCodeInManager(token: String, snippetId: UUID, updatedSnippet: SnippetDTO): SnippetDTO {
        val url = System.getenv("MANAGER_URI") + "/snippet/" + snippetId.toString()
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", token)
        val requestEntity = HttpEntity<SnippetDTO>(updatedSnippet, headers)

        val response: ResponseEntity<SnippetDTO> = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, SnippetDTO::class.java)
        return response.body!!

    }

    @Throws(HttpClientErrorException::class)
    fun getTest(token: String, testId: UUID): TestDTO {
        val url = System.getenv("MANAGER_URI") + "/test/" + testId.toString()
        val template = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", token)
        val requestEntity = HttpEntity<Void>(headers)

        val response: ResponseEntity<TestDTO> = template.exchange(url, HttpMethod.GET, requestEntity, TestDTO::class.java)
        return response.body!!

    }

    @Throws(HttpClientErrorException::class)
    fun getFormatterRules(token: String): FormatterRulesDTO {
        val url = System.getenv("MANAGER_URI") + "/rule/formatter"
        val template = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", token)
        val requestEntity = HttpEntity<Void>(headers)

        val response: ResponseEntity<FormatterRulesDTO> = template.exchange(url, HttpMethod.GET, requestEntity, FormatterRulesDTO::class.java)
        return response.body!!

    }

    @Throws(HttpClientErrorException::class)
    fun getLinterRules(token: String): LinterRulesDTO {
        val url = System.getenv("MANAGER_URI") + "/rule/linter"
        val template = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", token)
        val requestEntity = HttpEntity<Void>(headers)

        val response: ResponseEntity<LinterRulesDTO> = template.exchange(url, HttpMethod.GET, requestEntity, LinterRulesDTO::class.java)
        return response.body!!

    }


}