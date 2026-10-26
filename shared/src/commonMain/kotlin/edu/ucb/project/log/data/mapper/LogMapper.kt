package edu.ucb.project.log.data.mapper

import com.develoop.logs.ELogLevel
import com.develoop.logs.LogData
import edu.ucb.project.log.domain.model.LogLevel
import edu.ucb.project.log.domain.model.LogModel
import okio.ByteString.Companion.encodeUtf8
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// valores del dispositivo de ejemplo de la guía
private const val ANDROID_ID = "abc123"
private const val APP_INSTANCE_ID = "instance_001"
private const val VERSION_CODE = 123

@OptIn(ExperimentalTime::class)
fun LogModel.toLogData(): LogData {
    val now = Clock.System.now().toEpochMilliseconds()
    return LogData(
        androidId = ANDROID_ID.encodeUtf8(),
        appInstanceId = APP_INSTANCE_ID.encodeUtf8(),
        logLevel = level.toELogLevel(),
        message = message,
        stackTrace = stackTrace,
        serverTimeStamp = now,
        mobileTimeStamp = now,
        versionCode = VERSION_CODE,
        userId = userId
    )
}

private fun LogLevel.toELogLevel(): ELogLevel = when (this) {
    LogLevel.INFO -> ELogLevel.LEVEL_INFO
    LogLevel.DEBUG -> ELogLevel.LEVEL_DEBUG
    LogLevel.TRACE -> ELogLevel.LEVEL_TRACE
    LogLevel.WARN -> ELogLevel.LEVEL_WARN
    LogLevel.ERROR -> ELogLevel.LEVEL_ERROR
    LogLevel.FATAL -> ELogLevel.LEVEL_FATAL
}
