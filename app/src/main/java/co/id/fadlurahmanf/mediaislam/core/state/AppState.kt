package co.id.fadlurahmanf.mediaislam.core.state

import co.id.fadlurahmanf.mediaislam.core.network.exception.AladhanException


sealed class AppState<out T : Any> {
    data object IDLE : AppState<Nothing>()
    data object LOADING : AppState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : AppState<T>()
    data object ERROR : AppState<Nothing>()
}