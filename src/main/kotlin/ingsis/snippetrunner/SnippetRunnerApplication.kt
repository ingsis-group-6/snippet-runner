package ingsis.snippetrunner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.metrics.export.datadog.EnableDatadogMetrics

@SpringBootApplication
@EnableDatadogMetrics
class SnippetRunnerApplication

fun main(args: Array<String>) {
	runApplication<SnippetRunnerApplication>(*args)
}
