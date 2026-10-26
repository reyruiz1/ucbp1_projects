package edu.ucb.project.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import edu.ucb.project.firebasetest.domain.usecase.RunFirebaseSmokeTestUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogUploadWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

    private val uploadUseCase: RunFirebaseSmokeTestUseCase by inject()

    override suspend fun doWork(): Result {
        println("LogUploadWorker: ejecutar instrucción para subir datos")
        uploadUseCase.invoke()
        return Result.success()
    }
}
