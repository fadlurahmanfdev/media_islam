package co.id.fadlurahmanf.mediaislam.main.data.dto.model

data class AlarmPrayerTimeModel(
    val fajr: Item,
    val dhuhr: Item,
    val asr: Item,
    val maghrib: Item,
    val isha: Item,
) {
    data class Item(
        var isActive: Boolean = false,
        val isAlarmActive: Boolean = false,
        val time: String,
    )
}
