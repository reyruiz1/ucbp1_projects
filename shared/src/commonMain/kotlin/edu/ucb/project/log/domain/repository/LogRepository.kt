package edu.ucb.project.log.domain.repository

import edu.ucb.project.log.domain.model.LogModel

interface LogRepository {
    suspend fun sendLogs(serverUrl: String, logs: List<LogModel>): Result<Unit>
}
