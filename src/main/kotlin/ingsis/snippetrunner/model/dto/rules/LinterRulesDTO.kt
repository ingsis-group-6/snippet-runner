package ingsis.snippetrunner.model.dto.rules

import common.config.reader.linter.CaseConvention

data class LinterRulesDTO(
    var caseConvention: CaseConvention?,
    var printExpressionsEnabled: Boolean?,
)