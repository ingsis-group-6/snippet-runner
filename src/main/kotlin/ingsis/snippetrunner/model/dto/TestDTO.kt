package ingsis.snippetrunner.model.dto

import java.util.*

data class TestDTO(
    var description: String? = null,
    var input: List<String> = listOf(),
    var output: List<String> = listOf(),
    var snippetId: UUID? = null,
    var id: UUID? = null,
    var ownerId: String? = null,
    var snippet: SnippetDTO? = null,
)