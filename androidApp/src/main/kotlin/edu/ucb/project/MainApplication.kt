package edu.ucb.project

import android.app.Application
import edu.ucb.project.di.initKoinAndroid
import edu.ucb.project.firebasetest.runFirebaseSmokeTest
import edu.ucb.project.scheduler.LogScheduler

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
        runFirebaseSmokeTest()
        LogScheduler(this).schedulePeriodicalUpload()
    }
}


