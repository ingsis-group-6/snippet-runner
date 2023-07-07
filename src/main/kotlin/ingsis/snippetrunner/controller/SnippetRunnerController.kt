package ingsis.snippetrunner.controller

import ingsis.snippetrunner.model.dto.BaseSnippetRunnerDTO
import ingsis.snippetrunner.model.dto.RunnerOutputDTO
import ingsis.snippetrunner.model.dto.SnippetRunnerDTO
import ingsis.snippetrunner.redis.producer.LintResultProducer
import ingsis.snippetrunner.service.ServiceSelector
import lexer.implementation.Lexer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import snippet.events.lint.LintResultEvent
import snippet.events.lint.LintResultStatus


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
        val result = service.fetchAndLint(token, dto.snippetId, dto.version)
        return ResponseEntity(RunnerOutputDTO(result), HttpStatus.OK)

    }

    @PostMapping("/redis")
    suspend fun redis(@RequestHeader("Authorization") token: String, @RequestBody dto: BaseSnippetRunnerDTO): ResponseEntity<LintResultEvent> {
        val event = LintResultEvent(dto.snippetId, LintResultStatus.PENDING)
        this.lintResultProducer.publishEvent(event)
        return ResponseEntity(event, HttpStatus.OK)

    }

}
