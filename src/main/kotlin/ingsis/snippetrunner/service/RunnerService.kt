package ingsis.snippetrunner.service

import java.util.UUID

interface RunnerService {

    fun run(token: String, snippetId: UUID, languageVersion: String, inputList: List<String>)

    fun format(token: String, snippetId: UUID, languageVersion: String,): String

    fun lint(token: String, snippetId: UUID, languageVersion: String,): String


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