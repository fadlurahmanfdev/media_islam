package co.id.fadlurahmanf.mediaislam.quran.data.datasources

import co.id.fadlurahmanf.mediaislam.core.network.api.EQuranAPI
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran.DetailSurahResponse
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.quran.SurahResponse
import co.id.fadlurahmanf.mediaislam.core.network.exception.EQuranException
import co.id.fadlurahmanf.mediaislam.quran.data.dto.response.QariResponse
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class EQuranDatasourceRepositoryImpl @Inject constructor(
    private val eQuranAPI: EQuranAPI,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
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

    override fun getQari(): Observable<List<QariResponse>> {
        return Observable.create<List<QariResponse>> { emitter ->
            firebaseRemoteConfig.fetch(0).addOnSuccessListener {
                firebaseRemoteConfig.activate().addOnSuccessListener { isSuccess ->
                    val result = firebaseRemoteConfig.getString("QARI")
                    val model = Gson().fromJson<Array<QariResponse>>(result, Array<QariResponse>::class.java)
                    emitter.onNext(model.toList())
                }.addOnFailureListener {
                    emitter.onError(it)
                }.addOnCompleteListener {
                    emitter.onComplete()
                }
            }.addOnFailureListener {
                emitter.onError(it)
                emitter.onComplete()
            }
        }
    }

}