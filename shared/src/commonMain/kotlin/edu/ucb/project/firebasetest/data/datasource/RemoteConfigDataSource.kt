package edu.ucb.project.firebasetest.data.datasource

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig

class RemoteConfigDataSource {

    private val remoteConfig by lazy { Firebase.remoteConfig }

    suspend fun fetchAndActivate(): Boolean {
        return remoteConfig.fetchAndActivate()
    }

    fun getString(key: String): String {
        return remoteConfig.getValue(key).asString()
    }
}
