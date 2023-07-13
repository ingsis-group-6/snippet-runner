package ingsis.snippetrunner.utils

import ingsis.snippetrunner.model.dto.rules.LinterRulesDTO
import linter.implementations.FunctionExpressionLinter
import linter.implementations.IdentifierCaseLinter
import linter.implementations.SyntaxLinter
import linter.`interface`.Linter
import org.springframework.stereotype.Component
import snippet.events.lint.rules.LinterRules

@Component
class LinterRulesDecoder {

    fun decodeLinterRules(linterRules: LinterRulesDTO): Set<Linter> {
        val setToReturn = mutableSetOf<Linter>()
        setToReturn.add(IdentifierCaseLinter(linterRules.caseConvention!!))
        if (!linterRules.printExpressionsEnabled!!) setToReturn.add(FunctionExpressionLinter())
        setToReturn.add(SyntaxLinter())
        return setToReturn.toSet()
    }

    fun decodeLinterRules(linterRules: LinterRules): Set<Linter> {
        val setToReturn = mutableSetOf<Linter>()
        setToReturn.add(IdentifierCaseLinter(CaseConventionAdapter.toPrintscriptCaseConvention(linterRules.caseConvention)))
        if (!linterRules.printExpressionsEnabled) setToReturn.add(FunctionExpressionLinter())
        setToReturn.add(SyntaxLinter())
        return setToReturn.toSet()

    }
}