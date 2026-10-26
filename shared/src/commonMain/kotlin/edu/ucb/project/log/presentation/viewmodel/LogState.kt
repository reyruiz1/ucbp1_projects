package edu.ucb.project.log.presentation.viewmodel

import edu.ucb.project.log.domain.model.LogLevel

data class LogState(
    // emulador: http://10.0.2.2:9090 | ngrok: la URL https que muestra ngrok
    val serverUrl: String = "http://10.0.2.2:9090",
    val message: String = "Something went wrong",
    val level: LogLevel = LogLevel.ERROR,
    val isLoading: Boolean = false,
    val result: String? = null,
    val error: String? = null
)
