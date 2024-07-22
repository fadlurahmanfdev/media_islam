package co.id.fadlurahmanf.mediaislam.article.data.datasources

import co.id.fadlurahmanf.mediaislam.core.network.dto.response.BaseArtikelIslamResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import io.reactivex.rxjava3.core.Observable

interface ArticleDatasourceRepository {
    fun getIslamArticle(source: String, page:Int): Observable<BaseArtikelIslamResponse.Data<List<ArticleItemResponse>>>
}