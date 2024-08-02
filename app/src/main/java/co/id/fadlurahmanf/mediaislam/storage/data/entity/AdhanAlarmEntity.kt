package co.id.fadlurahmanf.mediaislam.storage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType
import co.id.fadlurahmanf.mediaislam.storage.other.AppDatabaseConstant
import co.id.fadlurahmanf.mediaislam.storage.other.type_converter.FullDateTypeConverter
import co.id.fadlurahmanf.mediaislam.storage.other.type_converter.PrayerTimeEnumTypeConverter
import java.util.Date

@Entity(tableName = AppDatabaseConstant.ADHAN_ALARM_ENTITY)
data class AdhanAlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @TypeConverters(PrayerTimeEnumTypeConverter::class)
    val type: PrayerTimeType? = null,
    val triggerDate: String? = null,
    val triggerTime: String? = null,
    @TypeConverters(FullDateTypeConverter::class)
    val triggerDateTime: Date? = null,
)
