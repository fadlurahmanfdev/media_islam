package co.id.fadlurahmanf.mediaislam.storage.data.datasource

import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AdhanAlarmEntity

interface PrayerTimeLocalDatasource {
    fun insert(entity: AdhanAlarmEntity): io.reactivex.Single<AdhanAlarmEntity>
    fun isAlarmExistByPrayerTimeTypeAndDate(type: PrayerTimeType, date:String): io.reactivex.Single<Boolean>
    fun getAdhanAlarmEntityByPrayerTimeType(type: PrayerTimeType): io.reactivex.Single<List<AdhanAlarmEntity>>
    fun getNextPrayerTimeByPrayerTimeType(type: PrayerTimeType): io.reactivex.Single<List<AdhanAlarmEntity>>
    fun deletePrayerTimeByPrayerTimeType(type: PrayerTimeType): io.reactivex.Single<Int>
}