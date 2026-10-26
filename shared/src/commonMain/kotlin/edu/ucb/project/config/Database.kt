package edu.ucb.project.config

import androidx.room3.ConstructedBy
import androidx.room3.AutoMigration
import edu.ucb.project.configlocal.data.dao.ConfigItemDao
import edu.ucb.project.configlocal.data.entity.ConfigItem
import edu.ucb.project.configlocal.data.entity.ConfigSeed
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import edu.ucb.project.movies.data.dao.DollarDao
import edu.ucb.project.movies.data.entity.DollarEntity

@Database(
    entities = [DollarEntity::class, ConfigItem::class, ConfigSeed::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DollarDao
    abstract fun getConfigItemDao(): ConfigItemDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
