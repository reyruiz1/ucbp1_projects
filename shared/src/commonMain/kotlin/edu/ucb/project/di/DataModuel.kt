package edu.ucb.project.di

import edu.ucb.project.Exam.data.dataSource.PeopleRemoteDataSource
import edu.ucb.project.Exam.data.repository.PeopleRepositoryImpl
import edu.ucb.project.Exam.data.service.PeopleService
import edu.ucb.project.Exam.domain.repository.PeopleRespository
import edu.ucb.project.catalogo.data.dataSource.CatalogRemoteDataSource
import edu.ucb.project.catalogo.data.repository.CatalogRepositoryImpl
import edu.ucb.project.catalogo.data.service.CatalogService
import edu.ucb.project.catalogo.domain.repository.CatalogRespository
import edu.ucb.project.userSearch.Data.repository.GithubRepositoryImpl
import edu.ucb.project.userSearch.Domain.repository.GithubRepository
import org.koin.dsl.module

val dataModule = module {
    single<GithubRepository> { GithubRepositoryImpl() }
    single<CatalogRemoteDataSource> { CatalogService() }
    single<CatalogRespository> { CatalogRepositoryImpl(get()) }
    single<PeopleRemoteDataSource> { PeopleService() }
    single<PeopleRespository> { PeopleRepositoryImpl(get()) }
}
