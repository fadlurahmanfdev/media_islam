package co.id.fadlurahmanf.mediaislam.alarm.domain.usecase

import co.id.fadlurahmanf.mediaislam.alarm.data.dto.model.NextPrayerTimeModel
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.AlarmPrayerTimeModel
import co.id.fadlurahmanf.mediaislam.storage.data.dto.model.AlarmPrayerTimeEntityModel
import io.reactivex.rxjava3.core.Observable

interface AlarmUseCase {
    fun getAlarmPrayerTime(): Observable<AlarmPrayerTimeModel>
    fun getNextPrayerTimeByLatitudeLongitude(
        latitude: Double,
        longitude: Double
    ): io.reactivex.Observable<NextPrayerTimeModel>

    fun saveAlarmPrayerTime(
        isFajrAdhanActive: Boolean?,
        isDhuhrAdhanActive: Boolean?,
        isAsrAdhanActive: Boolean?,
        isMaghribAdhanActive: Boolean?,
        isIshaAdhanActive: Boolean?,
    ): Observable<AlarmPrayerTimeEntityModel>
}