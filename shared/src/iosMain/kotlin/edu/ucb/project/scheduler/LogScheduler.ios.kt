package edu.ucb.project.scheduler

actual class LogScheduler actual constructor(
    context: Any?
) {
    actual fun schedulePeriodicalUpload() {
        // WorkManager solo existe en Android; en iOS se usaría BGTaskScheduler.
        println("LogScheduler: programación periódica no implementada en iOS")
    }
}
