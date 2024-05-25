package co.id.fadlurahmanf.mediaislam.main.data.dto.model

data class PrayersTimeModel(
    val readableDateInHijr: String,
    val readableDate: String,
    val location: String,
    val nextPrayerTime: String,
    val nextPrayer: String,
    val timing: Timing
) {
    data class Timing(
        val fajr: String,
        val dhuhr: String,
        val asr: String,
        val maghrib: String,
        val isha: String,
    )
}
