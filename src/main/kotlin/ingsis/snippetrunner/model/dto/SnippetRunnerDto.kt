package ingsis.snippetrunner.model.dto

import ingsis.snippetrunner.model.SupportedLanguage
import java.util.*

data class SnippetRunnerDto(
    val snippetId: UUID,
    val language: SupportedLanguage,
    val version: String,
    val inputs: List<String>
)