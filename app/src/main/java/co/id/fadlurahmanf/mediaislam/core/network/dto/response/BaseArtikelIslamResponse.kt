package co.id.fadlurahmanf.mediaislam.core.network.dto.response

import com.google.gson.annotations.SerializedName

data class BaseArtikelIslamResponse<T>(
    val status: Boolean = false,
    val message: String? = null,
    val data: Data<T>? = null,
) {
    data class Data<T>(
        val pagination: PaginationData? = null,
        @SerializedName("data")
        val content: T,
    ) {
        data class PaginationData(
            @SerializedName("total_page")
            val totalPage: String? = null,
        )
    }
}
