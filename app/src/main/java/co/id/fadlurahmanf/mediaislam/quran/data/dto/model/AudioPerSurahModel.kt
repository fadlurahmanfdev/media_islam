package co.id.fadlurahmanf.mediaislam.quran.data.dto.model

data class AudioPerSurahModel(
    val type: Int = 0,
    val url: String,
    val qari: Qari,
    val surah: Surah,
    var isExpanded:Boolean = false
) {
    data class Qari(
        val name: String,
        val image: String? = null,
        val imageKey: String? = null,
    )
    data class Surah(
        val no: Int,
        val latinName: String,
        val arabic: String,
        val meaning: String,
    )
}
