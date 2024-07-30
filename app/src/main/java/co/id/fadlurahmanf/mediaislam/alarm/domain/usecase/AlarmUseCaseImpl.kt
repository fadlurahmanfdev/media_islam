package co.id.fadlurahmanf.mediaislam.alarm.domain.usecase

import co.id.fadlurahmanf.mediaislam.core.network.exception.AladhanException
import co.id.fadlurahmanf.mediaislam.main.data.datasources.AladhanDatasourceRepository
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.AlarmPrayerTimeModel
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import io.reactivex.rxjava3.core.Observable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AlarmUseCaseImpl(
    private val aladhanDatasourceRepository: AladhanDatasourceRepository,
    private val corePlatformLocationRepository: CorePlatformLocationRepository,
    private val appLocalDatasource: AppLocalDatasource
) : AlarmUseCase {

    override fun getAlarmPrayerTime(): Observable<AlarmPrayerTimeModel> {
        return appLocalDatasource.getAlarmPrayerTime().flatMap { alarmEntity ->
            corePlatformLocationRepository.getCurrentAddress().flatMap { address ->
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val rawMonth = calendar.get(Calendar.MONTH)
                val month = rawMonth + 1
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val formattedDate = sdf.format(calendar.time)
                val indonesianSdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "id"))
                val indonesianFormattedDate = indonesianSdf.format(calendar.time)
                aladhanDatasourceRepository.getPrayersTime(
                    year = year,
                    month = month,
                    latitude = address.latitude,
                    longitude = address.longitude,
                ).map { response ->
                    val todayPrayerTime =
                        response.firstOrNull { element -> element.date?.gregorian?.date == formattedDate }
                    if (todayPrayerTime == null) throw AladhanException(
                        enumCode = "PRAYER_TIME_00",
                        message = "Prayer Time Missing"
                    )
                    AlarmPrayerTimeModel(
                        fajr = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isFajrAdhanAlarmActive,
                            time = todayPrayerTime.timings?.fajr ?: "-",
                        ),
                        dhuhr = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isDhuhrAdhanAlarmActive,
                            time = todayPrayerTime.timings?.dhuhr ?: "-",
                        ),
                        asr = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isAsrAdhanAlarmActive,
                            time = todayPrayerTime.timings?.asr ?: "-",
                        ),
                        maghrib = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isMaghribAdhanAlarmActive,
                            time = todayPrayerTime.timings?.maghrib ?: "-",
                        ),
                        isha = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isIshaAdhanAlarmActive,
                            time = todayPrayerTime.timings?.isha ?: "-",
                        )
                    )
                }
            }
        }
    }

    override fun saveAlarmPrayerTime(
        isFajrAdhanActive: Boolean?,
        isDhuhrAdhanActive: Boolean?,
        isAsrAdhanActive: Boolean?,
        isMaghribAdhanActive: Boolean?,
        isIshaAdhanActive: Boolean?
    ): Observable<Unit> {
        return appLocalDatasource.saveAlarmPrayerTime(
            isFajrAdhanActive = isFajrAdhanActive,
            isDhuhrAdhanActive = isDhuhrAdhanActive,
            isAsrAdhanActive = isAsrAdhanActive,
            isMaghribAdhanActive = isMaghribAdhanActive,
            isIshaAdhanActive = isIshaAdhanActive
        )
    }
}