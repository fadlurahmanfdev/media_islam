package co.id.fadlurahmanf.mediaislam.core.network.api

import co.id.fadlurahmanf.mediaislam.core.network.dto.response.BaseArtikelIslamResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtikeIslamAPI {
    @GET(".netlify/functions/api/{source}")
    fun getArticle(
        @Path("source") source: String,
        @Query("page") page:Int,
    ): Observable<Response<BaseArtikelIslamResponse<List<ArticleItemResponse>>>>
}