package edu.ucb.project.log.data.datasource

import com.develoop.logs.LogRequest
import com.develoop.logs.LogResponse
import edu.ucb.project.log.LogService

class LogsRemoteDataSource {
    // la URL de ngrok cambia en cada ejecución, se reutiliza el cliente mientras sea la misma
    private var serverUrl: String? = null
    private var service: LogService? = null

    suspend fun send(serverUrl: String, request: LogRequest): LogResponse {
        val current = service?.takeIf { this.serverUrl == serverUrl }
            ?: LogService(serverUrl).also {
                this.serverUrl = serverUrl
                service = it
            }
        return current.sendLogs(request)
    }
}
