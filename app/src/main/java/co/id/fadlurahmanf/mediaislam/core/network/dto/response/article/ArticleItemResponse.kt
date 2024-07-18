package co.id.fadlurahmanf.mediaislam.core.network.dto.response.article

import com.google.gson.annotations.SerializedName

data class ArticleItemResponse(
    val id: String? = null,
    val date: String? = null,
    @SerializedName("date_time")
    val dateTime: String? = null,
    val thumbnail: String? = null,
    val title: String? = null,
    val url: String? = null,
    val type: String? = null,
    val categories: List<Category>? = null,
) {
    data class Category(
        val name: String? = null,
        val url: String? = null,
    )
}