package edu.ucb.project.di

import edu.ucb.project.tareas.NotificadorTareasAndroid
import edu.ucb.project.tareas.domain.NotificadorTareas
import org.koin.dsl.module

// E50-5 · Implementaciones solo-Android (notificación, SharedPreferences)
val tareasAndroidModule = module {
    single<NotificadorTareas> { NotificadorTareasAndroid(get()) }
}
