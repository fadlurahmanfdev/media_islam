package co.id.fadlurahmanf.mediaislam.core.network.exception

class AladhanException(
    override val message: String,
    val httpCode: Int? = null,
    val enumCode: String? = null,
) : Throwable() {}

fun Throwable.fromAladhanException(): AladhanException {
    if (this is AladhanException) {
        return this
    } else {
        return AladhanException(message = message ?: "-")
    }
}
