package ingsis.snippetrunner.controller

import lexer.implementation.Lexer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import printscript.v1.app.StreamedExecution

@RestController
class SnippetRunnerController {
    @GetMapping
    fun hello(): String {
        val printscript = Lexer()
        return "Hello World!"
    }
}