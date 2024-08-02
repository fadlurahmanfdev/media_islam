package co.id.fadlurahmanf.mediaislam.core.network.api

import co.id.fadlurahmanf.mediaislam.core.network.dto.response.BaseAladhanResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.prayer_time.PrayersTimeResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AladhanAPI {
    @GET("calendar/{year}/{month}")
    fun getPrayersTime(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Observable<Response<BaseAladhanResponse<List<PrayersTimeResponse>>>>

    @GET("calendar/{year}/{month}")
    fun getPrayersTimeV2(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): io.reactivex.Observable<Response<BaseAladhanResponse<List<PrayersTimeResponse>>>>
}