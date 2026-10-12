package edu.ucb.project.exchange.data.repository

import kotlinx.coroutines.flow.Flow
import edu.ucb.project.exchange.data.datasource.RealTimeDataBase
import edu.ucb.project.exchange.domain.repository.ExchangeRepository

class ExchangeRepositoryImpl(
    val realTimeDataBase: RealTimeDataBase
): ExchangeRepository {
    override suspend fun observe(): Flow<String?> {
        return realTimeDataBase.observeMessage()
    }
}