package edu.ucb.project.di

import org.koin.core.module.dsl.singleOf
import edu.ucb.project.config.AppDatabase
import edu.ucb.project.exchange.data.datasource.RealTimeDataBase
import edu.ucb.project.exchange.data.repository.ExchangeRepositoryImpl
import edu.ucb.project.exchange.domain.repository.ExchangeRepository
import edu.ucb.project.movies.data.dao.DollarDao
import edu.ucb.project.movies.data.datasource.DollarLocalDataSource
import edu.ucb.project.movies.data.repository.DollarRepositoryImpl
import edu.ucb.project.movies.domain.repository.DollarRepository
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
import edu.ucb.project.firebasetest.data.datasource.FirestoreDataSource
import edu.ucb.project.firebasetest.data.datasource.MessagingDataSource
import edu.ucb.project.firebasetest.data.datasource.RemoteConfigDataSource
import edu.ucb.project.log.data.datasource.LogsRemoteDataSource
import edu.ucb.project.log.data.repository.LogRepositoryImpl
import edu.ucb.project.log.domain.repository.LogRepository
import org.koin.dsl.module

val dataModule = module {
    single<GithubRepository> { GithubRepositoryImpl() }
    single<CatalogRemoteDataSource> { CatalogService() }
    single<CatalogRespository> { CatalogRepositoryImpl(get()) }
    single<PeopleRemoteDataSource> { PeopleService() }
    single<PeopleRespository> { PeopleRepositoryImpl(get()) }
    single<DollarDao> {
        get<AppDatabase>().getDao()
    }
    singleOf(::DollarLocalDataSource)
    single<DollarRepository> { DollarRepositoryImpl(get()) }
    singleOf(::RealTimeDataBase)
    single<ExchangeRepository> { ExchangeRepositoryImpl(get()) }
    singleOf(::FirestoreDataSource)
    singleOf(::RemoteConfigDataSource)
    singleOf(::MessagingDataSource)
    singleOf(::LogsRemoteDataSource)
    single<LogRepository> { LogRepositoryImpl(get()) }
}
