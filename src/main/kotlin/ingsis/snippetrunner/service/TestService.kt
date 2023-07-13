package ingsis.snippetrunner.service

import ingsis.snippetrunner.model.dto.TestDTO
import ingsis.snippetrunner.model.dto.TestResultDTO
import java.util.UUID

interface TestService {
    fun runTest(token: String, testId: UUID, version: String): TestResultDTO
}