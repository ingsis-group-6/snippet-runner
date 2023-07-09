package ingsis.snippetrunner.service

import common.config.reader.formatter.CustomFormatterRules
import common.config.reader.formatter.FormatterRules
import common.io.Inputter
import common.io.Outputter
import ingsis.snippetrunner.language.printscript.ListOutputter
import ingsis.snippetrunner.language.printscript.StringListInputter
import ingsis.snippetrunner.model.dto.SnippetDTO
import ingsis.snippetrunner.utils.LinterRulesDecoder
import linter.`interface`.Linter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import printscript.v1.app.StreamedExecution
import printscript.v1.app.StreamedFormat
import printscript.v1.app.StreamedLint
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*

@Service
class PrintscriptRunnerService(
    @Autowired private val httpService: HttpService,
    @Autowired private val linterRulesDecoder: LinterRulesDecoder
    ): RunnerService {
    override fun fetchAndRun(token: String, snippetId: UUID, languageVersion: String, inputList: List<String>): List<String> {

        val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)

        return runSnippet(ByteArrayInputStream(snippetManagerResponse.content!!.toByteArray()), inputList, languageVersion)

    }

    override fun runSnippet(contentInputStream: InputStream, inputList: List<String>, languageVersion: String): List<String> {
        val inputter: Inputter = StringListInputter(inputList)
        val outputsList = mutableListOf<String>()
        val outputter: Outputter = ListOutputter(outputsList)
        StreamedExecution(contentInputStream, languageVersion, inputter, outputter).execute()
        return outputsList
    }

    override fun format(token: String, snippetId: UUID, languageVersion: String,): String {

        val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)
        val formatRules = this.httpService.getFormatterRules(token)
        val customRules = CustomFormatterRules(formatRules.spaceBeforeColon,formatRules.spaceAfterColon,formatRules.spaceBeforeAndAfterAssignationOperator,formatRules.newLinesBeforePrintLn,4)
        val stringAccumulator = StringAccumulator("")
        val concatOutputter: Outputter = StringAccumulatorOutputter(stringAccumulator)
        formatSnippet(snippetManagerResponse, languageVersion, concatOutputter, customRules)
        this.httpService.updateSnippetCodeInManager(token, snippetId, SnippetDTO(snippetManagerResponse.name, snippetManagerResponse.type, stringAccumulator.stringValue))
        return stringAccumulator.stringValue
    }


    override fun fetchAndLint(token: String, snippetId: UUID, languageVersion: String,): List<String> {
        val snippetManagerResponse = this.httpService.getSnippetCodeFromManager(token, snippetId)
        val rules = this.httpService.getLinterRules(token)
        val linterRules = this.linterRulesDecoder.decodeLinterRules(rules)
        return lint(snippetManagerResponse.content!!, languageVersion, linterRules)

    }

    override fun lint(
        snippetContent: String,
        languageVersion: String,
        linters: Set<Linter>
    ): List<String> {


        val outputsList: MutableList<String> = mutableListOf()
        val outputter: Outputter = ListOutputter(outputsList)
        val contentInputStream = ByteArrayInputStream(snippetContent.toByteArray())

        lintSnippet(contentInputStream, languageVersion, outputter, linters)
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