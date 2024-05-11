package co.id.fadlurahmanf.mediaislam.core.state

import co.id.fadlurahmanf.mediaislam.core.network.exception.EQuranException

sealed class EQuranNetworkState<out T : Any> {
    data object IDLE : EQuranNetworkState<Nothing>()
    data object LOADING : EQuranNetworkState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : EQuranNetworkState<T>()
    data class ERROR(
        val exception: EQuranException
    ) : EQuranNetworkState<Nothing>()
}