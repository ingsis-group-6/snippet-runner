package ingsis.snippetrunner.service

import ingsis.snippetrunner.model.dto.TestResultDTO
import java.io.InputStream
import java.util.UUID

interface RunnerService {

    fun fetchAndRun(token: String, snippetId: UUID, languageVersion: String, inputList: List<String>): List<String>
    fun runSnippet(contentInputStream: InputStream, inputList: List<String>, languageVersion: String): List<String>

    fun format(token: String, snippetId: UUID, languageVersion: String): String

    fun fetchAndLint(token: String, snippetId: UUID, languageVersion: String): List<String>
    fun lint(snippetContent: String, languageVersion: String): List<String>



}



/*

    1) Manager
    2) Share
    3) Runner

    5) [Ext.] Users - Auth0
    6) [Ext.] Datadog
    7) Redis

    7) Infra -> API(nginx), HTTPS


 */