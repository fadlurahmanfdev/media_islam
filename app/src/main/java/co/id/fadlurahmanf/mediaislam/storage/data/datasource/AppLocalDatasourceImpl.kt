package co.id.fadlurahmanf.mediaislam.storage.data.datasource

import co.id.fadlurahmanf.mediaislam.storage.data.dao.AppEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AppEntity
import io.reactivex.rxjava3.core.Observable

class AppLocalDatasourceImpl(
    private val appDao: AppEntityDao
) : AppLocalDatasource {
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
}