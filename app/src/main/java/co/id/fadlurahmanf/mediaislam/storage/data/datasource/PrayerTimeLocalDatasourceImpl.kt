package co.id.fadlurahmanf.mediaislam.storage.data.datasource

import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType
import co.id.fadlurahmanf.mediaislam.storage.data.dao.AdhanAlarmEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AdhanAlarmEntity
import io.reactivex.Single
import java.util.Calendar

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

    override fun isAlarmExistByPrayerTimeTypeAndDate(type: PrayerTimeType, date: String): Single<Boolean> {
        return adhanAlarmEntityDao.getAllByTypeAndDate(type.name, date)
            .map { alarms ->
                alarms.isNotEmpty()
            }
    }

    override fun getAdhanAlarmEntityByPrayerTimeType(type: PrayerTimeType): io.reactivex.Single<List<AdhanAlarmEntity>> {
        return adhanAlarmEntityDao.getAllByType(type.name)
    }

    override fun getNextPrayerTimeByPrayerTimeType(type: PrayerTimeType): Single<List<AdhanAlarmEntity>> {
        val now = Calendar.getInstance().time
        return adhanAlarmEntityDao.getAllByType(type.name).map { entities ->
            entities.filter { entity ->
                entity.triggerDateTime.after(now)
            }
        }
    }

    override fun deletePrayerTimeByPrayerTimeType(type: PrayerTimeType): Single<Int> {
        return adhanAlarmEntityDao.deleteByPrayerType(type.name)
    }
}