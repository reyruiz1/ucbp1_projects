package edu.ucb.project.configlocal.data.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import edu.ucb.project.configlocal.data.entity.ConfigItem
import edu.ucb.project.configlocal.data.entity.ConfigSeed
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ConfigItemDao {
    @Insert
    abstract suspend fun insert(item: ConfigItem)

    @Insert
    abstract suspend fun insertAll(items: List<ConfigItem>)

    @Query("SELECT * FROM config_items ORDER BY timestamp DESC, id DESC")
    abstract fun getAll(): Flow<List<ConfigItem>>

    @Query("DELETE FROM config_items WHERE id = :id")
    abstract suspend fun deleteById(id: Long)

    @Query("DELETE FROM config_items")
    abstract suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM config_items")
    abstract suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM config_seed WHERE id = 1")
    abstract suspend fun seedCount(): Int

    @Insert
    abstract suspend fun insertSeed(seed: ConfigSeed)

    @Transaction
    open suspend fun sembrarUnaVez(timestamp: Long) {
        if (seedCount() > 0) return
        if (count() == 0) {
            insertAll(listOf(
                ConfigItem(clave = "tema", valor = "claro", timestamp = timestamp),
                ConfigItem(clave = "idioma", valor = "es", timestamp = timestamp),
                ConfigItem(clave = "max_items", valor = "10", timestamp = timestamp)
            ))
        }
        insertSeed(ConfigSeed())
    }
}
