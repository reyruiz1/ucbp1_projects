package edu.ucb.project.di

import edu.ucb.project.catalogo.domain.usecase.GetMoviesUsecase
import edu.ucb.project.login.domain.usecase.LoginUseCase
import edu.ucb.project.userSearch.Domain.useCase.SearchUserUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::SearchUserUseCase)
    singleOf(::LoginUseCase)
    singleOf(::GetMoviesUsecase)
}
