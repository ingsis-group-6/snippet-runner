package ingsis.snippetrunner.service

import ingsis.snippetrunner.model.dto.TestResultDTO
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.util.*

@Component
class TestServiceImpl(private val httpService: HttpService, private val serviceSelector: ServiceSelector): TestService {
    override fun runTest(token: String, testId: UUID, version: String): TestResultDTO {
        val test = this.httpService.getTest(token, testId)
        val runnerService = this.serviceSelector.getRunnerService(enumValueOf(test.snippet!!.type!!))
        val result = runnerService.runSnippet(
            ByteArrayInputStream(test.snippet!!.content!!.toByteArray()),
            test.input,
            version
            )
        return TestResultDTO(testId, result == test.output, test.output, result)
    }
}