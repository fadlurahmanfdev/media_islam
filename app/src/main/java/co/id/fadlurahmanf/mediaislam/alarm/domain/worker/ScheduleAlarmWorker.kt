package co.id.fadlurahmanf.mediaislam.alarm.domain.worker

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import co.id.fadlurahmanf.mediaislam.alarm.data.dto.model.NextPrayerTimeModel
import co.id.fadlurahmanf.mediaislam.alarm.data.exception.AlarmWorkerException
import co.id.fadlurahmanf.mediaislam.alarm.domain.receiver.AlarmReceiver
import co.id.fadlurahmanf.mediaislam.alarm.domain.usecase.AlarmUseCase
import co.id.fadlurahmanf.mediaislam.alarm.domain.usecase.AlarmUseCaseImpl
import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType
import co.id.fadlurahmanf.mediaislam.core.enums.PrayerTimeType.*
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
import co.id.fadlurahmanf.mediaislam.storage.data.entity.AdhanAlarmEntity
import co.id.fadlurahmanf.mediaislam.storage.domain.AppDatabase
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepositoryImpl
import co.id.fadlurahmanfdev.kotlin_feature_alarm.domain.receiver.FeatureAlarmReceiver
import io.reactivex.Single
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScheduleAlarmWorker(val context: Context, workerParameters: WorkerParameters) :
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
    private lateinit var currentPrayerDate: String
    private lateinit var currentPrayerTime: String
    private lateinit var currentPrayerDateTime: Date
    private var latitude: Double = -1.0
    private var longitude: Double = -1.0

    companion object {
        const val PARAM_PRAYER_TIME_TYPE = "PARAM_PRAYER_TIME_TYPE"
        const val PARAM_CURRENT_PRAYER_TIME = "PARAM_CURRENT_PRAYER_TIME"
        const val PARAM_CURRENT_PRAYER_DATE = "PARAM_CURRENT_PRAYER_DATE"
        const val PARAM_CURRENT_PRAYER_DATE_TIME = "PARAM_CURRENT_PRAYER_DATE_TIME"
        const val PARAM_LATITUDE = "PARAM_LATITUDE"
        const val PARAM_LONGITUDE = "PARAM_LONGITUDE"
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
        currentPrayerDate = inputData.getString(PARAM_CURRENT_PRAYER_DATE)
            ?: throw AlarmWorkerException(reason = "currentPrayerDate is missing")
        currentPrayerTime = inputData.getString(PARAM_CURRENT_PRAYER_TIME)
            ?: throw AlarmWorkerException(reason = "currentPrayerTime is missing")
        val currentPrayerDateTimeString = inputData.getString(PARAM_CURRENT_PRAYER_DATE_TIME)
            ?: throw AlarmWorkerException(reason = "currentPrayerDateTimeString is missing")

        currentPrayerDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(
            currentPrayerDateTimeString
        ) ?: throw AlarmWorkerException(reason = "cannot parse currentPrayerDateTime")

        latitude = inputData.getDouble(PARAM_LATITUDE, -1.0)
        longitude = inputData.getDouble(PARAM_LONGITUDE, -1.0)

        if (latitude == -1.0 || longitude == -1.0) {
            throw AlarmWorkerException(reason = "Parameter Location Missing")
        }
    }

    private fun isNowBeforePrayerTime(prayerTime: Date): Boolean {
        val currentTime = Calendar.getInstance().time
        return currentTime.before(prayerTime)
    }

    private fun getAdhanAlarmEntityFromNextPrayerTime(
        type: PrayerTimeType,
        nextPrayer: NextPrayerTimeModel
    ): AdhanAlarmEntity {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val dateSdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val timeSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        var triggerDate: String? = null
        var triggerTime: String? = null
        var triggerDateTime: Date? = null
        when (type) {
            FAJR -> {
                triggerDateTime = sdf.parse(nextPrayer.fajr.dateTime)
                if (triggerDateTime != null) {
                    triggerDate = dateSdf.format(triggerDateTime)
                    triggerTime = timeSdf.format(triggerDateTime)
                }
            }

            DHUHR -> {
                triggerDateTime = sdf.parse(nextPrayer.dhuhr.dateTime)
                if (triggerDateTime != null) {
                    triggerDate = dateSdf.format(triggerDateTime)
                    triggerTime = timeSdf.format(triggerDateTime)
                }
            }

            ASR -> {
                triggerDateTime = sdf.parse(nextPrayer.asr.dateTime)
                if (triggerDateTime != null) {
                    triggerDate = dateSdf.format(triggerDateTime)
                    triggerTime = timeSdf.format(triggerDateTime)
                }
            }

            MAGHRIB -> {
                triggerDateTime = sdf.parse(nextPrayer.maghrib.dateTime)
                if (triggerDateTime != null) {
                    triggerDate = dateSdf.format(triggerDateTime)
                    triggerTime = timeSdf.format(triggerDateTime)
                }
            }

            ISHA -> {
                triggerDateTime = sdf.parse(nextPrayer.isha.dateTime)
                if (triggerDateTime != null) {
                    triggerDate = dateSdf.format(triggerDateTime)
                    triggerTime = timeSdf.format(triggerDateTime)
                }
            }
        }
        return AdhanAlarmEntity(
            type = prayerTimeType,
            triggerDate = triggerDate ?: "",
            triggerTime = triggerTime ?: "",
            triggerDateTime = triggerDateTime
        )
    }

    override fun createWork(): io.reactivex.Single<Result> {
        try {
            initDependencyClass()
            initInputData()
            Log.d(ScheduleAlarmWorker::class.java.simpleName, "success init data")

            if (isNowBeforePrayerTime(currentPrayerDateTime)) {
                Log.d(
                    ScheduleAlarmWorker::class.java.simpleName,
                    "now is before the current prayer time"
                )
                return prayerTimeLocalDatasource.isAlarmExistByPrayerTimeTypeAndDate(
                    prayerTimeType,
                    currentPrayerDate
                ).flatMap { isAlarmExist ->
                    Log.d(
                        ScheduleAlarmWorker::class.java.simpleName,
                        "alarm for $prayerTimeType at $currentPrayerDate is exist -> $isAlarmExist"
                    )
                    if (!isAlarmExist) {
                        val alarmEntity = AdhanAlarmEntity(
                            type = prayerTimeType,
                            triggerDate = currentPrayerDate,
                            triggerTime = currentPrayerTime,
                            triggerDateTime = currentPrayerDateTime
                        )
                        return@flatMap prayerTimeLocalDatasource.insert(alarmEntity).map { result ->
                            Log.d(
                                ScheduleAlarmWorker::class.java.simpleName,
                                "successfully insert entity of $result"
                            )
                            alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                currentPrayerDateTime.time,
                                FeatureAlarmReceiver.getPendingIntentSetAlarm(
                                    context,
                                    result.id ?: 0,
                                    AlarmReceiver::class.java,
                                )
                            )
                            Result.success(workDataOf("message" to "success set alarm to $currentPrayerDateTime"))
                        }
                    }
                    Single.just(Result.failure(workDataOf("reason" to "alarm for $prayerTimeType at $currentPrayerDate already exist")))
                }
            }

            Log.d(ScheduleAlarmWorker::class.java.simpleName, "init next date")
            val nextDate = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
            }.time
            val formattedNextDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(nextDate)
            Log.d(
                ScheduleAlarmWorker::class.java.simpleName,
                "successfully set formatted next date: $formattedNextDate"
            )

            return prayerTimeLocalDatasource.isAlarmExistByPrayerTimeTypeAndDate(
                prayerTimeType,
                formattedNextDate
            ).flatMap { isExist ->
                Log.d(
                    ScheduleAlarmWorker::class.java.simpleName,
                    "alarm for $prayerTimeType at $formattedNextDate is exist -> $isExist"
                )
                if (!isExist) {
                    return@flatMap Single.fromObservable(
                        alarmUseCase.getNextPrayerTimeByLatitudeLongitude(
                            latitude = latitude,
                            longitude = longitude
                        )
                    ).flatMap { nextPrayerTime ->
                        Log.d(
                            ScheduleAlarmWorker::class.java.simpleName,
                            "success fetched nextPrayerTime: $nextPrayerTime"
                        )
                        val alarmEntity = getAdhanAlarmEntityFromNextPrayerTime(
                            type = prayerTimeType,
                            nextPrayerTime
                        )
                        Log.d(
                            ScheduleAlarmWorker::class.java.simpleName,
                            "converted into entity: $alarmEntity"
                        )
                        prayerTimeLocalDatasource.insert(alarmEntity).map { result ->
                            Log.d(
                                ScheduleAlarmWorker::class.java.simpleName,
                                "success insert entity of $alarmEntity"
                            )
                            var message: String = ""
                            result.triggerDateTime.let { date ->
                                message = "success set alarm at $date"

                                setAlarm(date.time, result.id ?: 0)
                            }
                            Result.success(workDataOf("message" to "set alarm at $message"))
                        }
                    }
                }
                Single.just(Result.failure(workDataOf("reason" to "alarm for $prayerTimeType at $formattedNextDate already exist")))
            }
        } catch (e: AlarmWorkerException) {
            return Single.just(Result.failure(workDataOf("reason" to e.reason)))
        } catch (e: Exception) {
            return Single.just(Result.failure(workDataOf("reason" to "${e.message}")))
        }
    }

    private fun setAlarm(timemillis:Long, requestCode:Int){
        val pendingIntent = FeatureAlarmReceiver.getPendingIntentSetAlarm(
            context,
            requestCode = requestCode,
            clazz = AlarmReceiver::class.java
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, timemillis, pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, timemillis, pendingIntent
                )
            }
        }
    }
}