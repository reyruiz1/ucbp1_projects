package edu.ucb.project.di

import edu.ucb.project.config.AppDatabase
import edu.ucb.project.configlocal.data.dao.ConfigItemDao
import edu.ucb.project.configlocal.data.repository.ConfigRepositoryImpl
import edu.ucb.project.configlocal.domain.repository.ConfigRepository
import edu.ucb.project.configlocal.presentation.viewmodel.ConfigViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val configLocalModule = module {
    single<ConfigItemDao> { get<AppDatabase>().getConfigItemDao() }
    single<ConfigRepository> { ConfigRepositoryImpl(get()) }
    viewModelOf(::ConfigViewModel)
}
