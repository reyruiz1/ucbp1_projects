package edu.ucb.project.log.domain.model

data class LogModel(
    val level: LogLevel,
    val message: String,
    val stackTrace: String,
    val userId: String
)

enum class LogLevel { INFO, DEBUG, TRACE, WARN, ERROR, FATAL }
