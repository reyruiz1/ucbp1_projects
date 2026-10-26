package edu.ucb.project.log.data.repository

import com.develoop.logs.ELogResultCode
import com.develoop.logs.LogRequest
import edu.ucb.project.log.data.datasource.LogsRemoteDataSource
import edu.ucb.project.log.data.mapper.toLogData
import edu.ucb.project.log.domain.model.LogModel
import edu.ucb.project.log.domain.repository.LogRepository

class LogRepositoryImpl(val dataSource: LogsRemoteDataSource) : LogRepository {
    override suspend fun sendLogs(serverUrl: String, logs: List<LogModel>): Result<Unit> {
        return runCatching {
            val request = LogRequest(logs = logs.map { it.toLogData() })
            val response = dataSource.send(serverUrl, request)
            println("LogRepository: respuesta gRPC $response")
            if (response.resultCode != ELogResultCode.LOG_RESULT_OK) {
                error("El servidor respondió ${response.resultCode}")
            }
        }
    }
}
