package ingsis.snippetrunner.controller

import ingsis.snippetrunner.model.SupportedLanguage
import ingsis.snippetrunner.model.dto.BaseSnippetRunnerDTO
import ingsis.snippetrunner.model.dto.RunnerOutputDTO
import ingsis.snippetrunner.model.dto.SnippetRunnerDTO
import ingsis.snippetrunner.model.event.LintResultEvent
import ingsis.snippetrunner.redis.producer.LintResultProducer
import ingsis.snippetrunner.service.PrintscriptRunnerService
import ingsis.snippetrunner.service.RunnerService
import ingsis.snippetrunner.service.ServiceSelector
import lexer.implementation.Lexer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import printscript.v1.app.StreamedExecution

@RestController
class SnippetRunnerController(
    private val serviceSelector: ServiceSelector,
    private val lintResultProducer: LintResultProducer
) {
    @GetMapping
    fun hello(): String {
        val printscript = Lexer()
        return "Hello World!"
    }

    // lenguaje, version, id

    @PostMapping("/run")
    fun runSnippet(@RequestHeader("Authorization") token: String, @RequestBody dto: SnippetRunnerDTO): ResponseEntity<RunnerOutputDTO> {
        val service = this.serviceSelector.getRunnerService(dto.language)
        return ResponseEntity(RunnerOutputDTO(service.run(token, dto.snippetId, dto.version, dto.inputs)), HttpStatus.OK)

    }

    @PostMapping("/format")
    fun formatSnippet(@RequestHeader("Authorization") token: String, @RequestBody dto: BaseSnippetRunnerDTO): ResponseEntity<String> {
        val service = this.serviceSelector.getRunnerService(dto.language)
        val result = service.format(token, dto.snippetId, dto.version)
        return ResponseEntity(result, HttpStatus.OK)

    }

    @PostMapping("/lint")
    fun lintSnippet(@RequestHeader("Authorization") token: String, @RequestBody dto: BaseSnippetRunnerDTO): ResponseEntity<RunnerOutputDTO> {
        val service = this.serviceSelector.getRunnerService(dto.language)
        val result = service.lint(token, dto.snippetId, dto.version)
        return ResponseEntity(RunnerOutputDTO(result), HttpStatus.OK)

    }

    @PostMapping("/redis")
    suspend fun redis(@RequestHeader("Authorization") token: String, @RequestBody dto: BaseSnippetRunnerDTO): ResponseEntity<LintResultEvent> {
        val event = LintResultEvent(dto.snippetId, "finished")
        this.lintResultProducer.publishEvent(event)
        return ResponseEntity(event, HttpStatus.OK)

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