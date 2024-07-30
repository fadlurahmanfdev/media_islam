package co.id.fadlurahmanf.mediaislam.storage.data.datasource

import io.reactivex.rxjava3.core.Observable

interface AppLocalDatasource {
    fun getIsFirstInstall(): Observable<Boolean>

    fun saveIsNotFirstInstall(deviceId: String): Observable<Unit>
}