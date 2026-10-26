package edu.ucb.project

import android.app.Application
import edu.ucb.project.di.initKoinAndroid
import edu.ucb.project.firebasetest.runFirebaseSmokeTest
import edu.ucb.project.scheduler.LogScheduler
import edu.ucb.project.notificaciones.CanalNotificacionesE25

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        CanalNotificacionesE25.crear(this)
        initKoinAndroid(this)
        runFirebaseSmokeTest()
        LogScheduler(this).schedulePeriodicalUpload()
    }
}


