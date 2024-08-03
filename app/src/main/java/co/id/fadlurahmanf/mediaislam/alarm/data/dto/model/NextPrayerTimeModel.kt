package co.id.fadlurahmanf.mediaislam.alarm.data.dto.model

data class NextPrayerTimeModel(
    val fajr: Content,
    val dhuhr: Content,
    val asr: Content,
    val maghrib: Content,
    val isha: Content,
){
    data class Content(
        val date:String,
        val time:String,
        val dateTime:String,
    )
}
