package edu.ucb.project.log

import com.develoop.logs.LogRequest
import com.develoop.logs.LogResponse

actual class LogService actual constructor(
    serverUrl: String
) {
    actual suspend fun sendLogs(request: LogRequest): LogResponse {
        // El cliente gRPC de Wire usa OkHttp, que solo existe en Android.
        throw UnsupportedOperationException("gRPC no implementado en iOS")
    }
}
