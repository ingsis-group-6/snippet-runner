package ingsis.snippetrunner.service

import ingsis.snippetrunner.error.HTTPError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.util.*

@Service
class PrintscriptRunnerService(@Autowired private val httpService: HttpService): RunnerService {
    override fun run(token: String, snippetId: UUID, languageVersion: String) {

        try{
            val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)
            print(snippetManagerResponse.content)
        } catch (ex: HttpClientErrorException.NotFound) {
            throw HTTPError("Snippet not found", HttpStatus.NOT_FOUND)
        } catch (ex: HttpClientErrorException.Forbidden) {
            throw HTTPError("You are not allowed to access this snippet", HttpStatus.FORBIDDEN)
        } catch (ex: HttpClientErrorException.Unauthorized) {
            throw HTTPError("You are not authorized to access this snippet", HttpStatus.UNAUTHORIZED)
        } catch (ex: HttpClientErrorException) {
            throw HTTPError("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR)
        }




    }

    override fun format(code: String): String {
        TODO("Not yet implemented")
    }

    override fun lint(code: String): String {
        TODO("Not yet implemented")
    }


}