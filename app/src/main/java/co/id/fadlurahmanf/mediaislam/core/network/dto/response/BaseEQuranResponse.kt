package co.id.fadlurahmanf.mediaislam.core.network.dto.response

data class BaseEQuranResponse<T>(
    val code: Int? = null,
    val message: String? = null,
    val data: T
)
