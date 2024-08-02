package co.id.fadlurahmanf.mediaislam.storage.other.type_converter

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FullDateTypeConverter {

    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun fromDate(enum: Date): String {
        return sdf.format(enum)
    }

    @TypeConverter
    fun fromString(value: String): Date {
        return sdf.parse(value) ?: Calendar.getInstance().time
    }
}