package ingsis.snippetrunner.model.event

import java.util.UUID

data class LintResultEvent(
    val lintedSnippetId: UUID,
    val status: String
)