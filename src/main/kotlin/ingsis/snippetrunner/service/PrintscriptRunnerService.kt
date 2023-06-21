package ingsis.snippetrunner.service

import common.io.Inputter
import common.io.Outputter
import ingsis.snippetrunner.error.HTTPError
import ingsis.snippetrunner.language.printscript.ListOutputter
import ingsis.snippetrunner.language.printscript.StringListInputter
import linter.`interface`.Linter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import printscript.v1.app.StreamedExecution
import printscript.v1.app.StreamedLint
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

    override fun format(token: String, snippetId: UUID, languageVersion: String,): String {
        try{
            val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)
            // make a put request to snippet manager with new formatted code

            return ""

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

    override fun lint(token: String, snippetId: UUID, languageVersion: String,): String {
        try{
            val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)
            val linterRules: Set<Linter> = setOf() // TODO: get linter rules from snippet manager

            val outputsList: MutableList<String> = mutableListOf()
            val outputter: Outputter = ListOutputter(outputsList)
            val contentInputStream = ByteArrayInputStream(snippetManagerResponse.content!!.toByteArray())

            StreamedLint(contentInputStream, languageVersion, outputter, linterRules).execute()

            print(outputsList)

            return ""


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


}