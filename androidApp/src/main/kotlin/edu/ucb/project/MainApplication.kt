package edu.ucb.project

import android.app.Application
import edu.ucb.project.di.initKoinAndroid
import edu.ucb.project.firebasetest.runFirebaseSmokeTest
import edu.ucb.project.notificaciones.CanalesNotificacion
import edu.ucb.project.scheduler.LogScheduler
import edu.ucb.project.tareas.iniciarVigilanteTareas

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
        CanalesNotificacion.crear(this)
        iniciarVigilanteTareas()
        runFirebaseSmokeTest()
        LogScheduler(this).schedulePeriodicalUpload()
    }
}


