package edu.ucb.project.di

import edu.ucb.project.Exam.presentation.viewmodel.PeopleViewModel
import edu.ucb.project.catalogo.presentation.viewmodel.CatalogViewModel
import edu.ucb.project.login.presentation.state.LoginViewModel
import edu.ucb.project.userSearch.Presentation.viewModel.UserSearchViewModel
import org.koin.core.module.dsl.viewModelOf
import edu.ucb.project.movies.presentation.viewmodel.DollarViewModel
import edu.ucb.project.exchange.presentation.viewmodel.ExchangeViewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::UserSearchViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::CatalogViewModel)
    viewModelOf(::PeopleViewModel)
    viewModelOf(::DollarViewModel)
    viewModelOf(::ExchangeViewModel)
}
