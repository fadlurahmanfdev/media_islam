package co.id.fadlurahmanf.mediaislam.storage.other

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE ${AppDatabaseConstant.APP_ENTITY_TABLE_NAME} ADD COLUMN isFajrAdhanAlarmActive INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE ${AppDatabaseConstant.APP_ENTITY_TABLE_NAME} ADD COLUMN isDhuhrAdhanAlarmActive INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE ${AppDatabaseConstant.APP_ENTITY_TABLE_NAME} ADD COLUMN isAsrAdhanAlarmActive INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE ${AppDatabaseConstant.APP_ENTITY_TABLE_NAME} ADD COLUMN isMaghribAdhanAlarmActive INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE ${AppDatabaseConstant.APP_ENTITY_TABLE_NAME} ADD COLUMN isIshaAdhanAlarmActive INTEGER NOT NULL DEFAULT 0")
    }
}