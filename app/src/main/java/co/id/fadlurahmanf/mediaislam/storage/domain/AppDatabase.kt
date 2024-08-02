package co.id.fadlurahmanf.mediaislam.storage.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.id.fadlurahmanf.mediaislam.storage.data.dao.AdhanAlarmEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.dao.AppEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AdhanAlarmEntity
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AppEntity
import co.id.fadlurahmanf.mediaislam.storage.other.AppDatabaseConstant
import co.id.fadlurahmanf.mediaislam.storage.other.MIGRATION_1_2
import co.id.fadlurahmanf.mediaislam.storage.other.MIGRATION_2_3
import co.id.fadlurahmanf.mediaislam.storage.other.type_converter.FullDateTypeConverter
import co.id.fadlurahmanf.mediaislam.storage.other.type_converter.PrayerTimeEnumTypeConverter

@Database(
    entities = [
        AppEntity::class,
        AdhanAlarmEntity::class,
    ],
    version = AppDatabase.VERSION,
    exportSchema = true,
)
@TypeConverters(
    PrayerTimeEnumTypeConverter::class,
    FullDateTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appEntityDao(): AppEntityDao
    abstract fun adhanAlarmEntityDao(): AdhanAlarmEntityDao

    companion object {
        const val VERSION = 3

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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}