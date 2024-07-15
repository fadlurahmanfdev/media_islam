package co.id.fadlurahmanf.mediaislam.storage.data.datasource

import co.id.fadlurahmanf.mediaislam.storage.data.dao.AppEntityDao
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
}