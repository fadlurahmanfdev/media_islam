package co.id.fadlurahmanf.mediaislam.core.state

import co.id.fadlurahmanf.mediaislam.core.network.exception.AladhanException


sealed class AladhanNetworkState<out T : Any> {
    data object IDLE : AladhanNetworkState<Nothing>()
    data object LOADING : AladhanNetworkState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : AladhanNetworkState<T>()
    data class ERROR(
        val exception: AladhanException
    ) : AladhanNetworkState<Nothing>()
}