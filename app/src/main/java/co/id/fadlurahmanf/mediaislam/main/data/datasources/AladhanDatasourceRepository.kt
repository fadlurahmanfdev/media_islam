package co.id.fadlurahmanf.mediaislam.main.data.datasources

import co.id.fadlurahmanf.mediaislam.core.network.dto.response.prayer_time.PrayersTimeResponse
import io.reactivex.rxjava3.core.Observable

interface AladhanDatasourceRepository {
    fun getPrayersTime(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
    ): Observable<List<PrayersTimeResponse>>
}