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

val MIGRATION_2_3 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE ${AppDatabaseConstant.ADHAN_ALARM_ENTITY} IF NOT EXIST (`id` INTEGER, `type` VARCHAR(10), `triggerTime` VARCHAR(50), `triggerDate` VARCHAR(50), `triggerDateTime` VARCHAR(50), PRIMARY KEY(`id`))")
    }
}