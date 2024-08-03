package co.id.fadlurahmanf.mediaislam.storage.data.datasource

import co.id.fadlurahmanf.mediaislam.storage.data.dto.model.AlarmPrayerTimeEntityModel
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AdhanAlarmEntity
import io.reactivex.Single
import io.reactivex.rxjava3.core.Observable

interface AppLocalDatasource {
    fun getIsFirstInstall(): Observable<Boolean>
    fun saveIsNotFirstInstall(deviceId: String): Observable<Unit>
    fun getAlarmPrayerTime(): Observable<AlarmPrayerTimeEntityModel>
    fun saveAlarmPrayerTime(
        isFajrAdhanActive: Boolean?,
        isDhuhrAdhanActive: Boolean?,
        isAsrAdhanActive: Boolean?,
        isMaghribAdhanActive: Boolean?,
        isIshaAdhanActive: Boolean?,
    ): Observable<Unit>
}