package edu.ucb.project.di

import edu.ucb.project.tareas.VigilanteTareas
import edu.ucb.project.tareas.data.datasource.TareasFirestoreDataSource
import edu.ucb.project.tareas.data.repository.TareasRepositoryImpl
import edu.ucb.project.tareas.domain.repository.TareasRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import edu.ucb.project.tareas.presentation.viewmodel.TareasViewModel
import org.koin.core.module.dsl.viewModelOf


val tareasModule = module {
    singleOf(::TareasFirestoreDataSource)
    single<TareasRepository> { TareasRepositoryImpl(get()) }
    singleOf(::VigilanteTareas)
    viewModelOf(::TareasViewModel)
}
