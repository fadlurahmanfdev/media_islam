package co.id.fadlurahmanf.mediaislam.core.network.dto.response

data class BaseAladhanResponse<T>(
    val code: Int? = null,
    val status: String? = null,
    val data: T
)
