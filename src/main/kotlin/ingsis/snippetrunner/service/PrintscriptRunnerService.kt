package ingsis.snippetrunner.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.util.*

@Service
class PrintscriptRunnerService(@Autowired private val httpService: HttpService): RunnerService {
    override fun run(token: String, snippetId: UUID, languageVersion: String) {
        try{
            val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)
            print(snippetManagerResponse.content)
        } catch (exc: HttpClientErrorException) {
            println("Exception: ${exc.message}")
        }


    }

    override fun format(code: String): String {
        TODO("Not yet implemented")
    }

    override fun lint(code: String): String {
        TODO("Not yet implemented")
    }


}