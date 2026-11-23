package edu.ucb.project.di

import edu.ucb.project.puenteweb.presentation.viewmodel.PuenteWebViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val puenteWebModule = module {
    viewModelOf(::PuenteWebViewModel)
}
