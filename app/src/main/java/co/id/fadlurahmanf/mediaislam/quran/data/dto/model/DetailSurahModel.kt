package co.id.fadlurahmanf.mediaislam.quran.data.dto.model

data class DetailSurahModel(
    val surahNo: Int,
    val arabic: String,
    val latin: String,
    val totalVerse: Int,
    val origin: String,
    val meaning: String,
    val desc: String,
    val audioFull: List<Audio>,
    val verses: List<Verse>,
) {
    data class Audio(
        val url: String,
        val qariId: String,
    )

    data class Verse(
        val no: Int,
        val arabicText: String,
        val latinText: String,
        val indonesianText: String,
        val audio: List<Audio>,
    )
}
