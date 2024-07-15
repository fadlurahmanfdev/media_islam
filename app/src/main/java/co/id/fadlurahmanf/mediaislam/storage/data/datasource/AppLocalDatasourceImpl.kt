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
            println("MASUK ENTITY GET FIRST INSTALL: $entity")
            return@map entity.isFirstInstall
        }
    }

    override fun saveIsNotFirstInstall(): Observable<Unit> {
        return appDao.getAll().toObservable().map { entities ->
            if (entities.isEmpty()) {
                var entity = AppEntity(deviceId = "DEVICE_ID ASAL")
                entity = entity.copy(isFirstInstall = false)
                println("MASUK ENTITY SAVE FIRST INSTALL: $entity")
                return@map appDao.insert(entity)
            }
        }
    }
}