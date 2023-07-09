package ingsis.snippetrunner.utils

import common.config.reader.linter.CaseConvention

object CaseConventionAdapter {
    fun toPrintscriptCaseConvention(eventCaseConventionValue: snippet.events.lint.rules.CaseConvention): CaseConvention {
        return when (eventCaseConventionValue) {
            snippet.events.lint.rules.CaseConvention.CAMEL_CASE -> CaseConvention.CAMEL_CASE
            snippet.events.lint.rules.CaseConvention.SNAKE_CASE -> CaseConvention.SNAKE_CASE
        }
    }
}