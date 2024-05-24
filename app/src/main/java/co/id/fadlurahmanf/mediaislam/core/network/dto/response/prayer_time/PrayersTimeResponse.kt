package co.id.fadlurahmanf.mediaislam.core.network.dto.response.prayer_time

data class PrayersTimeResponse(
    val timings: Timing? = null,
    val date: Date? = null
) {
    data class Timing(
        val fajr: String? = null,
        val dhuhr: String? = null,
        val asr: String? = null,
        val maghrib: String? = null,
        val isha: String? = null,
    )

    data class Date(
        val gregorian: TypedCalendar? = null,
        val hijri: TypedCalendar? = null,
    ) {
        data class TypedCalendar(
            val date: String? = null,
            val day: String? = null,
            val weekday: Weekday? = null,
            val year: String? = null,
            val month: Month? = null,
            val holidays: List<String>? = null,
        ) {
            data class Weekday(
                val en: String? = null,
            )

            data class Month(
                val number: Int? = null,
                val en: String? = null,
                val ar: String? = null,
            )
        }
    }
}
