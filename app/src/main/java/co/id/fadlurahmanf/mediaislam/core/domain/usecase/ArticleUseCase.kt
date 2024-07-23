package co.id.fadlurahmanf.mediaislam.core.domain.usecase

import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import io.reactivex.rxjava3.core.Observable

interface ArticleUseCase {
    fun getTop3Article(): Observable<List<ArticleItemResponse>>
}