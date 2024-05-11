package co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran

import com.google.gson.annotations.SerializedName

data class SurahResponse(
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
)
