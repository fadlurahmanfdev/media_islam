package co.id.fadlurahmanf.mediaislam.quran.data.dto.model

data class SurahModel(
    val surahNo: Int,
    val arabic: String,
    val latin: String,
    val totalVerse: Int,
    val origin: String,
    val meaning: String,
)
