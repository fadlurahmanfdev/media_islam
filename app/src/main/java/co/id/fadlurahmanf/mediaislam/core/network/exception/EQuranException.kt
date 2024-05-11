package co.id.fadlurahmanf.mediaislam.core.network.exception

class EQuranException(
    override val message: String,
    val code: Int? = null,
) : Throwable() {}

fun Throwable.fromEQuranException(): EQuranException {
    if (this is EQuranException) {
        return this
    } else {
        return EQuranException(message = message ?: "-")
    }
}
