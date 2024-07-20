package co.id.fadlurahmanf.mediaislam.storage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.id.fadlurahmanf.mediaislam.storage.other.AppDatabaseConstant

@Entity(tableName = AppDatabaseConstant.APP_ENTITY_TABLE_NAME)
data class AppEntity(
    @PrimaryKey
    val deviceId: String,
    val isFirstInstall: Boolean = true,
)