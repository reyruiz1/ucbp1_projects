package edu.ucb.project.configlocal.domain.repository

import edu.ucb.project.configlocal.domain.model.ConfigModel
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    fun observarTodos(): Flow<List<ConfigModel>>
    suspend fun agregar(clave: String, valor: String)
    suspend fun borrar(id: Long)
    suspend fun borrarTodos()
    suspend fun sembrarUnaVez()
}
