package co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran

import com.google.gson.annotations.SerializedName

data class DetailSurahResponse(
    @SerializedName("nomor")
    val surahNo: Int? = null,
    @SerializedName("nama")
    val arabic: String? = null,
    @SerializedName("namaLatin")
    val latin: String? = null,
    @SerializedName("jumlahAyat")
    val totalVerse: Int? = null,
    @SerializedName("tempatTurun")
    val origin: String? = null,
    @SerializedName("arti")
    val meaning: String? = null,
    @SerializedName("deskripsi")
    val desc: String? = null,
    @SerializedName("ayat")
    val verses: List<Verse>? = null,
) {
    data class Verse(
        @SerializedName("nomorAyat")
        val no: Int? = null,
        @SerializedName("teksArab")
        val arabicText: String? = null,
        @SerializedName("teksLatin")
        val latinText: String? = null,
        @SerializedName("teksIndonesia")
        val indonesianText: String? = null,
    )
}
