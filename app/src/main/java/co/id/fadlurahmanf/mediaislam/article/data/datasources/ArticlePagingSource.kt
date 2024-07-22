package co.id.fadlurahmanf.mediaislam.article.data.datasources

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.BaseArtikelIslamResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ArticlePagingSource(
    private val articleDatasourceRepository: ArticleDatasourceRepository
) : RxPagingSource<Int, ArticleItemResponse>() {
    companion object {
        const val DEFAULT_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleItemResponse>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ArticleItemResponse>> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        val result = articleDatasourceRepository.getIslamArticle(source = "ks", page = page)
        return Single.fromObservable(result.subscribeOn(Schedulers.io())
            .map { responseData ->
                toLoadResult(
                    data = responseData,
                    page = page,
                    totalPage = (responseData.pagination?.totalPage ?: "").toInt()
                )
            })
    }

    private fun toLoadResult(
        data: BaseArtikelIslamResponse.Data<List<ArticleItemResponse>>,
        page: Int,
        totalPage: Int
    ): LoadResult<Int, ArticleItemResponse> {
        return LoadResult.Page(
            data = data.content,
            prevKey = if (page > 1) page - 1 else null,
            nextKey = if (page < totalPage) page + 1 else null
        )
    }
}