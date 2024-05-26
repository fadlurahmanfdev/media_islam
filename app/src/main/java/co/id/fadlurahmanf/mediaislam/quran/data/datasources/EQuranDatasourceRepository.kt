package co.id.fadlurahmanf.mediaislam.quran.data.datasources

import co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran.DetailSurahResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran.SurahResponse
import co.id.fadlurahmanf.mediaislam.quran.data.dto.response.QariResponse
import io.reactivex.rxjava3.core.Observable

interface EQuranDatasourceRepository {
    fun getListSurah(): Observable<List<SurahResponse>>
    fun getDetailSurah(surahNo:Int): Observable<DetailSurahResponse>
    fun getQari():Observable<List<QariResponse>>
}