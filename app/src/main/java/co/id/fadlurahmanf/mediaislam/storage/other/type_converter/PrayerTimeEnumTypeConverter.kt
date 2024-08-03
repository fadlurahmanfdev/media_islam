package co.id.fadlurahmanf.mediaislam.storage.other.type_converter

import androidx.room.TypeConverter
import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType

class PrayerTimeEnumTypeConverter {

    @TypeConverter
    fun fromEnum(enum: PrayerTimeType): String {
        return enum.name
    }

    @TypeConverter
    fun fromString(value: String): PrayerTimeType {
        return PrayerTimeType.valueOf(value)
    }
}