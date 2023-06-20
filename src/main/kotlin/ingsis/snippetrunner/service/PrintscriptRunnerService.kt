package ingsis.snippetrunner.service

import ingsis.snippetrunner.error.HTTPError
import ingsis.snippetrunner.language.printscript.ListOutputter
import ingsis.snippetrunner.language.printscript.StringListInputter
import interpreter.input.Inputter
import interpreter.output.Outputter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import printscript.v1.app.StreamedExecution
import java.io.ByteArrayInputStream
import java.util.*

@Service
class PrintscriptRunnerService(@Autowired private val httpService: HttpService): RunnerService {
    override fun run(token: String, snippetId: UUID, languageVersion: String, inputList: List<String>) {

        try{
            val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)
            print(snippetManagerResponse.content)

            val outputsList: MutableList<String> = mutableListOf()

            val inputter: Inputter = StringListInputter(inputList)
            val outputter: Outputter = ListOutputter(outputsList)

            val contentInputStream = ByteArrayInputStream(snippetManagerResponse.content!!.toByteArray())

            StreamedExecution(contentInputStream, languageVersion, inputter, outputter).execute()

            print(outputsList)

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