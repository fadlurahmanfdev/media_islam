package co.id.fadlurahmanf.mediaislam.article.data.datasources

import co.id.fadlurahmanf.mediaislam.core.network.api.ArtikeIslamAPI
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.BaseArtikelIslamResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ArticleDatasourceRepositoryImpl @Inject constructor(
    val artikeIslamAPI: ArtikeIslamAPI
):ArticleDatasourceRepository {
    override fun getKsArticle(): Observable<BaseArtikelIslamResponse.Data<List<ArticleItemResponse>>> {
        return artikeIslamAPI.getArticle(
            source = "ks"
        ).doOnNext { element ->
//            if (!element.isSuccessful) {
//                throw EQuranException(
//                    message = element.errorBody()?.string() ?: "",
//                    httpCode = element.code(),
//                    enumCode = "GET_DETAIL_00"
//                )
//            }
        }.map { element ->
            element.body()?.data!!
        }
    }
}