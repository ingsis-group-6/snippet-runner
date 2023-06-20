package ingsis.snippetrunner.service

import java.util.UUID

interface RunnerService {

    fun run(token: String, snippetId: UUID, languageVersion: String)

    fun format(code: String): String

    fun lint(code: String): String


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