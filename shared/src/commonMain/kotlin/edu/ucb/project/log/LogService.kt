package edu.ucb.project.log

import com.develoop.logs.LogRequest
import com.develoop.logs.LogResponse

// El cliente gRPC (OkHttp) solo existe en Android; cada plataforma da su implementación.
expect class LogService(serverUrl: String) {
    suspend fun sendLogs(request: LogRequest): LogResponse
}
