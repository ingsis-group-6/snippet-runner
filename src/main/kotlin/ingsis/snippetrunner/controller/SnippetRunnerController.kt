package ingsis.snippetrunner.controller

import ingsis.snippetrunner.model.SupportedLanguage
import ingsis.snippetrunner.model.dto.SnippetRunnerDTO
import ingsis.snippetrunner.service.PrintscriptRunnerService
import ingsis.snippetrunner.service.RunnerService
import ingsis.snippetrunner.service.ServiceSelector
import lexer.implementation.Lexer
import org.springframework.web.bind.annotation.*
import printscript.v1.app.StreamedExecution

@RestController
class SnippetRunnerController(private val serviceSelector: ServiceSelector) {
    @GetMapping
    fun hello(): String {
        val printscript = Lexer()
        return "Hello World!"
    }

    // lenguaje, version, id

    @PostMapping("/run")
    fun runSnippet(@RequestHeader("Authorization") token: String, @RequestBody dto: SnippetRunnerDTO) {
        val service = this.serviceSelector.getRunnerService(dto.language)
        return service.run(token, dto.snippetId, dto.version, dto.inputs)

    }








}



/*

    1) Pedis correr un snippet en el front
    2) llamas desde el front a serverUrl.com/runner/run/{snippet_id} con jwt -> nginx redirige al runner service
    3) runner service llama al manager service para pedir el snippet
    4) el manager verifica si es dueño o compartido (halbla con el share)
     - Si no, devuelve 403 y se termina
    5) corre el codigo del snippet y devuelve el output





 */