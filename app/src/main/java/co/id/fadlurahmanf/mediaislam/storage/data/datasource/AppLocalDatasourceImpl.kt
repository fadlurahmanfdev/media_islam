package co.id.fadlurahmanf.mediaislam.storage.data.datasource

import co.id.fadlurahmanf.mediaislam.storage.data.dao.AppEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.dto.model.AlarmPrayerTimeEntityModel
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AppEntity
import io.reactivex.rxjava3.core.Observable

class AppLocalDatasourceImpl(
    private val appDao: AppEntityDao
) : AppLocalDatasource {
    private fun getAppEntity(): Observable<AppEntity> {
        return appDao.getAll().toObservable().map { entities ->
            if (entities.isEmpty()) {
                throw Exception()
            }
            val entity = entities.first()
            return@map entity
        }
    }

    override fun getIsFirstInstall(): Observable<Boolean> {
        return appDao.getAll().toObservable().map { entities ->
            if (entities.isEmpty()) {
                return@map true
            }
            val entity = entities.first()
            return@map entity.isFirstInstall
        }
    }

    override fun saveIsNotFirstInstall(deviceId: String): Observable<Unit> {
        return appDao.getAll().toObservable().map { entities ->
            if (entities.isEmpty()) {
                var entity = AppEntity(deviceId = deviceId)
                entity = entity.copy(isFirstInstall = false)
                return@map appDao.insert(entity)
            }
        }
    }

    override fun getAlarmPrayerTime(): Observable<AlarmPrayerTimeEntityModel> {
        return getAppEntity().map { entity ->
            return@map AlarmPrayerTimeEntityModel(
                isFajrAdhanAlarmActive = entity.isFajrAdhanAlarmActive,
                isDhuhrAdhanAlarmActive = entity.isDhuhrAdhanAlarmActive,
                isAsrAdhanAlarmActive = entity.isAsrAdhanAlarmActive,
                isMaghribAdhanAlarmActive = entity.isMaghribAdhanAlarmActive,
                isIshaAdhanAlarmActive = entity.isIshaAdhanAlarmActive,
            )
        }
    }

    override fun saveAlarmPrayerTime(
        isFajrAdhanActive: Boolean?,
        isDhuhrAdhanActive: Boolean?,
        isAsrAdhanActive: Boolean?,
        isMaghribAdhanActive: Boolean?,
        isIshaAdhanActive: Boolean?
    ): Observable<Unit> {
        return getAppEntity().map { entity ->
            val newEntity = entity.copy(
                isFajrAdhanAlarmActive = isFajrAdhanActive ?: entity.isFajrAdhanAlarmActive,
                isDhuhrAdhanAlarmActive = isDhuhrAdhanActive ?: entity.isDhuhrAdhanAlarmActive,
                isAsrAdhanAlarmActive = isAsrAdhanActive ?: entity.isAsrAdhanAlarmActive,
                isMaghribAdhanAlarmActive = isMaghribAdhanActive
                    ?: entity.isMaghribAdhanAlarmActive,
                isIshaAdhanAlarmActive = isIshaAdhanActive ?: entity.isIshaAdhanAlarmActive,
            )
            return@map appDao.update(newEntity)
        }
    }
}