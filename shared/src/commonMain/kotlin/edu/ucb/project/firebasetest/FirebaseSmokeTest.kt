package edu.ucb.project.firebasetest

import edu.ucb.project.firebasetest.domain.usecase.RunFirebaseSmokeTestUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

// Llamar una sola vez, después de iniciar Koin.
fun runFirebaseSmokeTest() {
    val useCase = KoinPlatform.getKoin().get<RunFirebaseSmokeTestUseCase>()
    CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
        useCase.invoke()
    }
}
