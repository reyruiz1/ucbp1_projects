package edu.ucb.project.di

import edu.ucb.project.puenteweb.PreferenciasPuenteWebAndroid
import edu.ucb.project.puenteweb.domain.PreferenciasPuenteWeb
import org.koin.dsl.module

val puenteWebAndroidModule = module {
    single<PreferenciasPuenteWeb> { PreferenciasPuenteWebAndroid(get()) }
}
