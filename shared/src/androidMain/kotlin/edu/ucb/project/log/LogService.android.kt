package edu.ucb.project.log

import com.develoop.logs.LogRequest
import com.develoop.logs.LogResponse
import com.develoop.logs.LogServiceClient
import com.squareup.wire.GrpcClient
import okhttp3.OkHttpClient
import okhttp3.Protocol

actual class LogService actual constructor(
    serverUrl: String
) {
    // https (ngrok) negocia HTTP/2 con TLS; http (10.0.2.2 / localhost) necesita HTTP/2 sin TLS (h2c)
    private val protocols = if (serverUrl.startsWith("https://")) {
        listOf(Protocol.HTTP_2, Protocol.HTTP_1_1)
    } else {
        listOf(Protocol.H2_PRIOR_KNOWLEDGE)
    }

    private val httpClient = OkHttpClient.Builder()
        .protocols(protocols)
        .build()

    private val grpcClient = GrpcClient.Builder()
        .client(httpClient)
        .baseUrl(serverUrl)
        .build()

    private val logService = grpcClient.create(LogServiceClient::class)

    actual suspend fun sendLogs(request: LogRequest): LogResponse {
        return logService.send().execute(request)
    }
}
