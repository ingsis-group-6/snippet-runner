package ingsis.snippetrunner.model.dto.rules

data class FormatterRulesDTO(
    var spaceBeforeColon: Int,
    var spaceAfterColon: Int,
    var spaceBeforeAndAfterAssignationOperator: Int,
    var newLinesBeforePrintLn: Int
)
