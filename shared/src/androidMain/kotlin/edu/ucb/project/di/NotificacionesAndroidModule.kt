
package edu.ucb.project.di

import edu.ucb.project.notificaciones.NotificadorLocalAndroid
import edu.ucb.project.notificaciones.domain.NotificadorLocal
import org.koin.dsl.module

val notificacionesAndroidModule = module {
    single<NotificadorLocal> {
        NotificadorLocalAndroid(get())
    }
}
