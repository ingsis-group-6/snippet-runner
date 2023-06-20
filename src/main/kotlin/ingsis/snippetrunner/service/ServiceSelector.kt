package ingsis.snippetrunner.service

import ingsis.snippetrunner.model.SupportedLanguage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class ServiceSelector(@Autowired private val applicationContext: ApplicationContext) {

    fun getRunnerService(language: SupportedLanguage): RunnerService {
        return when(language) {
            SupportedLanguage.PRINTSCRIPT -> {
                val httpService = applicationContext.getBean(HttpService::class.java)
                PrintscriptRunnerService(httpService)
            }
        }
    }

}