package edu.ucb.project.configlocal.data.repository

import edu.ucb.project.configlocal.data.dao.ConfigItemDao
import edu.ucb.project.configlocal.data.entity.ConfigItem
import edu.ucb.project.configlocal.domain.model.ConfigModel
import edu.ucb.project.configlocal.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ConfigRepositoryImpl(private val dao: ConfigItemDao) : ConfigRepository {
    override fun observarTodos(): Flow<List<ConfigModel>> =
        dao.getAll().map { lista ->
            lista.map { ConfigModel(it.id, it.clave, it.valor, it.timestamp) }
        }

    override suspend fun agregar(clave: String, valor: String) {
        require(clave.isNotBlank() && valor.isNotBlank()) { "Completa clave y valor." }
        dao.insert(ConfigItem(clave = clave.trim(), valor = valor.trim(), timestamp = ahora()))
    }

    override suspend fun borrar(id: Long) = dao.deleteById(id)
    override suspend fun borrarTodos() = dao.deleteAll()
    override suspend fun sembrarUnaVez() = dao.sembrarUnaVez(ahora())

    @OptIn(ExperimentalTime::class)
    private fun ahora(): Long = Clock.System.now().toEpochMilliseconds()
}
