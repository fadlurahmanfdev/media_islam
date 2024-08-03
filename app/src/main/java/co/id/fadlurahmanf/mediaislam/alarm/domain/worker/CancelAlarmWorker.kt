package co.id.fadlurahmanf.mediaislam.alarm.domain.worker

import android.app.AlarmManager
import android.content.Context
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import co.id.fadlurahmanf.mediaislam.alarm.data.exception.AlarmWorkerException
import co.id.fadlurahmanf.mediaislam.alarm.domain.receiver.AlarmReceiver
import co.id.fadlurahmanf.mediaislam.alarm.domain.usecase.AlarmUseCase
import co.id.fadlurahmanf.mediaislam.alarm.domain.usecase.AlarmUseCaseImpl
import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType
import co.id.fadlurahmanf.mediaislam.core.network.api.AladhanAPI
import co.id.fadlurahmanf.mediaislam.core.network.other.NetworkUtility
import co.id.fadlurahmanf.mediaislam.main.data.datasources.AladhanDatasourceRepository
import co.id.fadlurahmanf.mediaislam.main.data.datasources.AladhanDatasourceRepositoryImpl
import co.id.fadlurahmanf.mediaislam.storage.data.dao.AdhanAlarmEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.dao.AppEntityDao
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasourceImpl
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.PrayerTimeLocalDatasource
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.PrayerTimeLocalDatasourceImpl
import co.id.fadlurahmanf.mediaislam.storage.domain.AppDatabase
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepositoryImpl
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.receiver.FeatureAlarmReceiver
import io.reactivex.Single
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class CancelAlarmWorker(val context: Context, workerParameters: WorkerParameters) :
    RxWorker(context, workerParameters) {

    private lateinit var aladhanApi: AladhanAPI
    private lateinit var appEntityDao: AppEntityDao
    private lateinit var adhanAlarmEntityDao: AdhanAlarmEntityDao
    private lateinit var aladhanDatasourceRepository: AladhanDatasourceRepository
    private lateinit var corePlatformLocationRepository: CorePlatformLocationRepository
    private lateinit var appLocalDatasource: AppLocalDatasource
    private lateinit var prayerTimeLocalDatasource: PrayerTimeLocalDatasource
    private lateinit var alarmUseCase: AlarmUseCase
    private lateinit var alarmManager: AlarmManager

    private lateinit var prayerTimeType: PrayerTimeType

    companion object {
        const val PARAM_PRAYER_TIME_TYPE = "PARAM_PRAYER_TIME_TYPE"
    }

    private fun initDependencyClass() {
        if (!::aladhanApi.isInitialized) {
            aladhanApi = NetworkUtility.provideAladhanNetwork(
                context,
                callAdapterFactory = RxJava2CallAdapterFactory.create()
            )
        }

        if (!::appEntityDao.isInitialized) {
            appEntityDao = AppDatabase.getDatabase(context).appEntityDao()
        }

        if (!::adhanAlarmEntityDao.isInitialized) {
            adhanAlarmEntityDao = AppDatabase.getDatabase(context).adhanAlarmEntityDao()
        }

        if (!::appLocalDatasource.isInitialized) {
            appLocalDatasource = AppLocalDatasourceImpl(
                appDao = appEntityDao,
            )
        }

        if (!::prayerTimeLocalDatasource.isInitialized) {
            prayerTimeLocalDatasource = PrayerTimeLocalDatasourceImpl(
                adhanAlarmEntityDao = adhanAlarmEntityDao,
            )
        }

        if (!::aladhanDatasourceRepository.isInitialized) {
            aladhanDatasourceRepository = AladhanDatasourceRepositoryImpl(
                aladhanAPI = aladhanApi
            )
        }

        if (!::corePlatformLocationRepository.isInitialized) {
            corePlatformLocationRepository = CorePlatformLocationRepositoryImpl(context)
        }

        if (!::alarmUseCase.isInitialized) {
            alarmUseCase = AlarmUseCaseImpl(
                aladhanDatasourceRepository = aladhanDatasourceRepository,
                appLocalDatasource = appLocalDatasource,
                corePlatformLocationRepository = corePlatformLocationRepository
            )
        }

        if (!::alarmManager.isInitialized) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
    }

    private fun initInputData() {
        val prayerTimeTypeString = inputData.getString(PARAM_PRAYER_TIME_TYPE)
        prayerTimeType = PrayerTimeType.valueOf(prayerTimeTypeString!!)
    }

    override fun createWork(): io.reactivex.Single<Result> {
        try {
            initDependencyClass()
            initInputData()
            Log.d(CancelAlarmWorker::class.java.simpleName, "success init data")


            return prayerTimeLocalDatasource.getNextPrayerTimeByPrayerTimeType(prayerTimeType)
                .flatMap { entities ->
                    entities.forEach { entity ->
                        try {
                            alarmManager.cancel(
                                FeatureAlarmReceiver.getPendingIntentSetAlarm(
                                    context,
                                    requestCode = entity.id ?: 0,
                                    bundle = null,
                                    clazz = AlarmReceiver::class.java,
                                )
                            )
                            Log.d(CancelAlarmWorker::class.java.simpleName, "Success cancel Alarm")
                        } catch (e: Throwable) {
                            Log.e(
                                CancelAlarmWorker::class.java.simpleName,
                                "cannot cancel alarm caused by ${e.message}"
                            )
                        }
                    }
                    prayerTimeLocalDatasource.deletePrayerTimeByPrayerTimeType(
                        prayerTimeType
                    ).map { count ->
                        Log.d(CancelAlarmWorker::class.java.simpleName, "success delete $count entity/entities from table")
                        Result.success()
                    }
                }
        } catch (e: AlarmWorkerException) {
            return Single.just(Result.failure(workDataOf("reason" to e.reason)))
        } catch (e: Exception) {
            return Single.just(Result.failure(workDataOf("reason" to "${e.message}")))
        }
    }
}