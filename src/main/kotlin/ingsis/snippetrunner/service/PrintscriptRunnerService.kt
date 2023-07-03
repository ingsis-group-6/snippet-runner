package ingsis.snippetrunner.service

import common.config.reader.formatter.CustomFormatterRules
import common.config.reader.formatter.FormatterRules
import common.config.reader.linter.CaseConvention
import common.io.Inputter
import common.io.Outputter
import ingsis.snippetrunner.error.HTTPError
import ingsis.snippetrunner.language.printscript.ListOutputter
import ingsis.snippetrunner.language.printscript.StringListInputter
import ingsis.snippetrunner.model.dto.SnippetDTO
import linter.implementations.IdentifierCaseLinter
import linter.`interface`.Linter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import printscript.v1.app.StreamedExecution
import printscript.v1.app.StreamedFormat
import printscript.v1.app.StreamedLint
import java.io.ByteArrayInputStream
import java.util.*

@Service
class PrintscriptRunnerService(@Autowired private val httpService: HttpService): RunnerService {
    override fun run(token: String, snippetId: UUID, languageVersion: String, inputList: List<String>): List<String> {

        val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)

        val outputsList: MutableList<String> = mutableListOf()

        val inputter: Inputter = StringListInputter(inputList)
        val outputter: Outputter = ListOutputter(outputsList)

        val contentInputStream = ByteArrayInputStream(snippetManagerResponse.content!!.toByteArray())

        StreamedExecution(contentInputStream, languageVersion, inputter, outputter).execute()
        return outputsList

    }

    override fun format(token: String, snippetId: UUID, languageVersion: String,): String {

        val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)
        val customRules = CustomFormatterRules(0,1,1,1,4)
        val stringAccumulator = StringAccumulator("")
        val concatOutputter: Outputter = StringAccumulatorOutputter(stringAccumulator)
        formatSnippet(snippetManagerResponse, languageVersion, concatOutputter, customRules)
        this.httpService.updateSnippetCodeInManager(token, snippetId, SnippetDTO(snippetManagerResponse.name, snippetManagerResponse.type, stringAccumulator.stringValue))
        return stringAccumulator.stringValue
    }


    override fun lint(token: String, snippetId: UUID, languageVersion: String,): List<String> {
        val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)
        val linterRules: Set<Linter> = setOf(IdentifierCaseLinter(CaseConvention.CAMEL_CASE)) // TO``DO: get linter rules from snippet manager

        val outputsList: MutableList<String> = mutableListOf()
        val outputter: Outputter = ListOutputter(outputsList)
        val contentInputStream = ByteArrayInputStream(snippetManagerResponse.content!!.toByteArray())

        lintSnippet(contentInputStream, languageVersion, outputter, linterRules)
        print(outputsList)
        return outputsList

    }

    private fun formatSnippet(
        snippetManagerResponse: SnippetDTO,
        languageVersion: String,
        concatOutputter: Outputter,
        customRules: CustomFormatterRules
    ) {
        StreamedFormat(
            ByteArrayInputStream(snippetManagerResponse.content!!.toByteArray()),
            languageVersion,
            concatOutputter,
            FormatterRules(customRules)
        ).execute()
    }
    private fun lintSnippet(
        contentInputStream: ByteArrayInputStream,
        languageVersion: String,
        outputter: Outputter,
        linterRules: Set<Linter>
    ) {
        StreamedLint(contentInputStream, languageVersion, outputter, linterRules).execute()
    }


}

class StringAccumulator(var stringValue: String) {
    fun concat(text: String) {
        stringValue += text
    }

}

class StringAccumulatorOutputter(private val stringAccumulator: StringAccumulator): Outputter {
    override fun output(text: String) {
        if(text != "EOF") stringAccumulator.concat(text)
    }
}