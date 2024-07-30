package co.id.fadlurahmanf.mediaislam.alarm.domain.usecase

import co.id.fadlurahmanf.mediaislam.main.data.dto.model.AlarmPrayerTimeModel
import io.reactivex.rxjava3.core.Observable

interface AlarmUseCase {
    fun getAlarmPrayerTime(): Observable<AlarmPrayerTimeModel>
    fun saveAlarmPrayerTime(
        isFajrAdhanActive: Boolean?,
        isDhuhrAdhanActive: Boolean?,
        isAsrAdhanActive: Boolean?,
        isMaghribAdhanActive: Boolean?,
        isIshaAdhanActive: Boolean?,
    ): Observable<Unit>
}