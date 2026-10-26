package edu.ucb.project.log.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.project.log.domain.model.LogModel
import edu.ucb.project.log.domain.usecase.SendLogsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LogViewModel(val usecase: SendLogsUseCase) : ViewModel() {
    private val _state = MutableStateFlow(LogState())
    val state = _state.asStateFlow()

    fun emitEvent(event: LogEvent) {
        when (event) {
            is LogEvent.ServerUrlChanged -> _state.update { it.copy(serverUrl = event.value.trim()) }
            is LogEvent.MessageChanged -> _state.update { it.copy(message = event.value) }
            is LogEvent.LevelSelected -> _state.update { it.copy(level = event.level) }
            LogEvent.SendPressed -> if (!_state.value.isLoading) sendLog()
        }
    }

    private fun sendLog() {
        val current = _state.value
        val log = LogModel(
            level = current.level,
            message = current.message,
            stackTrace = "Stacktrace here...",
            userId = "user_42"
        )
        _state.update { it.copy(isLoading = true, result = null, error = null) }
        viewModelScope.launch {
            usecase.invoke(current.serverUrl, listOf(log)).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false, result = "LOG_RESULT_OK") }
                },
                onFailure = { throwable ->
                    _state.update { it.copy(isLoading = false, error = throwable.message ?: throwable.toString()) }
                }
            )
        }
    }
}
