package co.id.fadlurahmanf.mediaislam.main.data.datasources

import co.id.fadlurahmanf.mediaislam.core.network.api.AladhanAPI
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.prayer_time.PrayersTimeResponse
import co.id.fadlurahmanf.mediaislam.core.network.exception.EQuranException
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AladhanDatasourceRepositoryImpl @Inject constructor(
    private val aladhanAPI: AladhanAPI
) : AladhanDatasourceRepository {

    override fun getPrayersTime(year: Int, month: Int, latitude: Double, longitude: Double): Observable<List<PrayersTimeResponse>> {
        return aladhanAPI.getPrayersTime(
            year = year,
            month = month,
            latitude = latitude,
            longitude = longitude
        ).doOnNext { element ->
            if (!element.isSuccessful) {
                throw EQuranException(
                    message = element.errorBody()?.string() ?: "",
                    httpCode = element.code(),
                    enumCode = "GET_DETAIL_00"
                )
            }
        }.map { element ->
            element.body()?.data!!
        }
    }

}