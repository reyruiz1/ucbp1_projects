package edu.ucb.project.log

import com.develoop.logs.LogRequest
import com.develoop.logs.LogResponse


expect class LogService(serverUrl: String) {
    suspend fun sendLogs(request: LogRequest): LogResponse
}
