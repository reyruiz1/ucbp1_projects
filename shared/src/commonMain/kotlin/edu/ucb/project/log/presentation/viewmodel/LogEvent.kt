package edu.ucb.project.log.presentation.viewmodel

import edu.ucb.project.log.domain.model.LogLevel

sealed interface LogEvent {
    data class ServerUrlChanged(val value: String) : LogEvent
    data class MessageChanged(val value: String) : LogEvent
    data class LevelSelected(val level: LogLevel) : LogEvent
    object SendPressed : LogEvent
}
