package edu.ucb.project.log.domain.usecase

import edu.ucb.project.log.domain.model.LogModel
import edu.ucb.project.log.domain.repository.LogRepository

class SendLogsUseCase(val repository: LogRepository) {
    suspend fun invoke(serverUrl: String, logs: List<LogModel>): Result<Unit> {
        return repository.sendLogs(serverUrl, logs)
    }
}
