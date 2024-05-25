package co.id.fadlurahmanf.mediaislam.core.network.api

import co.id.fadlurahmanf.mediaislam.core.network.dto.response.BaseEQuranResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran.DetailSurahResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran.SurahResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EQuranAPI {
    @GET("v2/surat")
    fun getSurahList(): Observable<BaseEQuranResponse<List<SurahResponse>>>

    @GET("v2/surat/{surahNo}")
    fun getDetailSurah(
        @Path("surahNo") surahNo: Int
    ): Observable<Response<BaseEQuranResponse<DetailSurahResponse>>>
}