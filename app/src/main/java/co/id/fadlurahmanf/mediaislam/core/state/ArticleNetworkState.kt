package co.id.fadlurahmanf.mediaislam.core.state

import co.id.fadlurahmanf.mediaislam.core.network.exception.AladhanException


sealed class ArticleNetworkState<out T : Any> {
    data object IDLE : ArticleNetworkState<Nothing>()
    data object LOADING : ArticleNetworkState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : ArticleNetworkState<T>()
    data object ERROR : ArticleNetworkState<Nothing>()
}