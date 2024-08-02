package co.id.fadlurahmanf.mediaislam.storage.data.datasource

import co.id.fadlurahmanf.mediaislam.storage.data.dao.AdhanAlarmEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AdhanAlarmEntity
import io.reactivex.Single

class PrayerTimeLocalDatasourceImpl(
    private val adhanAlarmEntityDao: AdhanAlarmEntityDao
) : PrayerTimeLocalDatasource {

    override fun insert(entity: AdhanAlarmEntity): Single<AdhanAlarmEntity> {
        return adhanAlarmEntityDao.insert(entity).flatMap {
            adhanAlarmEntityDao.getAll().map { alarms ->
                alarms.last()
            }
        }
    }

    override fun isAlarmExistByPrayerTimeTypeAndDate(type: String, date: String): Single<Boolean> {
        return adhanAlarmEntityDao.getAllByTypeAndDate(type, date)
            .map { alarms ->
                alarms.isNotEmpty()
            }
    }

    override fun getAdhanAlarmEntityByType(type: String): io.reactivex.Single<List<AdhanAlarmEntity>> {
        return adhanAlarmEntityDao.getAllByType(type)
    }
}