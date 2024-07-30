package co.id.fadlurahmanf.mediaislam.storage.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.id.fadlurahmanf.mediaislam.storage.data.dao.AppEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AppEntity
import co.id.fadlurahmanf.mediaislam.storage.other.AppDatabaseConstant
import co.id.fadlurahmanf.mediaislam.storage.other.MIGRATION_1_2

@Database(
    entities = [
        AppEntity::class
    ], version = AppDatabase.VERSION,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appEntityDao(): AppEntityDao

    companion object {
        const val VERSION = 2

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    AppDatabaseConstant.APP_DB_NAME
                )
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}