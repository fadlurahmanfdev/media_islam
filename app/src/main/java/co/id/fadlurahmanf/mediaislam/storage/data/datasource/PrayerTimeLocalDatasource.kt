package co.id.fadlurahmanf.mediaislam.storage.data.datasource

import co.id.fadlurahmanf.mediaislam.storage.data.entity.AdhanAlarmEntity
import io.reactivex.Single

interface PrayerTimeLocalDatasource {
    fun insert(entity: AdhanAlarmEntity): io.reactivex.Single<AdhanAlarmEntity>
    fun isAlarmExistByPrayerTimeTypeAndDate(type: String, date:String): io.reactivex.Single<Boolean>
    fun getAdhanAlarmEntityByType(type: String): io.reactivex.Single<List<AdhanAlarmEntity>>
}