package co.id.fadlurahmanf.mediaislam.quran.data.datasources

import co.id.fadlurahmanf.mediaislam.core.network.api.EQuranAPI
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran.DetailSurahResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran.SurahResponse
import co.id.fadlurahmanf.mediaislam.core.network.exception.EQuranException
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class EQuranDatasourceRepositoryImpl @Inject constructor(
    private val eQuranAPI: EQuranAPI
) : EQuranDatasourceRepository {

    override fun getListSurah(): Observable<List<SurahResponse>> {
        return eQuranAPI.getSurahList().map {
            it.data
        }
    }

    override fun getDetailSurah(surahNo: Int): Observable<DetailSurahResponse> {
        return eQuranAPI.getDetailSurah(surahNo).doOnNext { element ->
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