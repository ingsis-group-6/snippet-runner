package ingsis.snippetrunner.model.dto

import ingsis.snippetrunner.model.SupportedLanguage
import java.util.*

data class SnippetRunnerDTO(
    val snippetId: UUID,
    val language: SupportedLanguage,
    val version: String
)