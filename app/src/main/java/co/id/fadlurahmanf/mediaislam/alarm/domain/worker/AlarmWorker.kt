package co.id.fadlurahmanf.mediaislam.alarm.domain.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import co.id.fadlurahmanf.mediaislam.storage.data.dao.AppEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasourceImpl
import co.id.fadlurahmanf.mediaislam.storage.domain.AppDatabase
import io.reactivex.Observable
import io.reactivex.Single

class AlarmWorker(val context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters) {
    private lateinit var appEntityDao: AppEntityDao
    private lateinit var appLocalDatasource: AppLocalDatasource

    private fun setup() {
        if (!::appEntityDao.isInitialized) {
            appEntityDao = AppDatabase.getDatabase(context).appEntityDao()
        }

        if (!::appLocalDatasource.isInitialized) {
            appLocalDatasource = AppLocalDatasourceImpl(appDao = appEntityDao)
        }
    }

    override fun createWork(): io.reactivex.Single<Result> {
        setup()
        return Single.fromObservable(appLocalDatasource.getAlarmPrayerTimeV2()).map {
            println("MASUK ENTITY ${it}")
            Result.success()
        }
    }
}