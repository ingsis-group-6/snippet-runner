package ingsis.snippetrunner.model.dto

import java.util.*

data class TestResultDTO(
    var testId: UUID,
    var result: Boolean,
    var expected: List<String>,
    var actual: List<String>
)
