package co.id.fadlurahmanf.mediaislam.article.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import co.id.fadlurahmanf.mediaislam.article.data.datasources.ArticleDatasourceRepository
import co.id.fadlurahmanf.mediaislam.article.data.datasources.ArticlePagingSource
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ArticleViewModel @Inject constructor(
    private val articleDatasourceRepository: ArticleDatasourceRepository
) : ViewModel() {
    val compositeDisposable = CompositeDisposable()

    private val _pagingDataLive = MutableLiveData<PagingData<ArticleItemResponse>>()
    val pagingData: LiveData<PagingData<ArticleItemResponse>> = _pagingDataLive

    fun requestPagingArticle() {
        compositeDisposable.add(getPagingVideo().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _pagingDataLive.value = it
                }, {}, {}
            )
        )
    }


    private fun getPagingVideo(): Flowable<PagingData<ArticleItemResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
            ),
            pagingSourceFactory = {
                ArticlePagingSource(articleDatasourceRepository)
            }
        ).flowable.cachedIn(viewModelScope)
    }
}