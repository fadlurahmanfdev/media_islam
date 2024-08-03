package co.id.fadlurahmanf.mediaislam.alarm.domain.usecase

import co.id.fadlurahmanf.mediaislam.alarm.data.dto.model.NextPrayerTimeModel
import co.id.fadlurahmanf.mediaislam.core.network.exception.AladhanException
import co.id.fadlurahmanf.mediaislam.main.data.datasources.AladhanDatasourceRepository
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.AlarmPrayerTimeModel
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import co.id.fadlurahmanf.mediaislam.storage.data.dto.model.AlarmPrayerTimeEntityModel
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
                    val oldFormatDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    val newFormatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val newDate = newFormatDate.format(oldFormatDate.parse(todayPrayerTime.date?.gregorian?.date ?: "") ?: Calendar.getInstance().time)
                    AlarmPrayerTimeModel(
                        latitude = address.latitude,
                        longitude = address.longitude,
                        fajr = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isFajrAdhanAlarmActive,
                            date = newDate,
                            time = todayPrayerTime.timings?.fajr?.take(5) ?: "-",
                            dateTime = "$newDate ${todayPrayerTime.timings?.fajr?.take(5)}",
                            readableTime = todayPrayerTime.timings?.fajr ?: "-"
                        ),
                        dhuhr = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isDhuhrAdhanAlarmActive,
                            date = newDate,
                            time = todayPrayerTime.timings?.dhuhr?.take(5) ?: "-",
                            dateTime = "$newDate ${todayPrayerTime.timings?.dhuhr?.take(5)}",
                            readableTime = todayPrayerTime.timings?.dhuhr ?: "-",
                        ),
                        asr = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isAsrAdhanAlarmActive,
                            date = newDate,
                            time = todayPrayerTime.timings?.asr?.take(5) ?: "-",
                            dateTime = "$newDate ${todayPrayerTime.timings?.asr?.take(5)}",
                            readableTime = todayPrayerTime.timings?.asr ?: "-",
                        ),
                        maghrib = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isMaghribAdhanAlarmActive,
                            date = newDate,
                            time = todayPrayerTime.timings?.maghrib?.take(5) ?: "-",
                            dateTime = "$newDate ${todayPrayerTime.timings?.maghrib?.take(5)}",
                            readableTime = todayPrayerTime.timings?.maghrib ?: "-",
                        ),
                        isha = AlarmPrayerTimeModel.Item(
                            isActive = true,
                            isAlarmActive = alarmEntity.isIshaAdhanAlarmActive,
                            date = newDate,
                            time = todayPrayerTime.timings?.isha?.take(5) ?: "-",
                            dateTime = "$newDate ${todayPrayerTime.timings?.isha?.take(5)}",
                            readableTime = todayPrayerTime.timings?.isha ?: "-",
                        )
                    )
                }
            }
        }
    }

    override fun getNextPrayerTimeByLatitudeLongitude(
        latitude: Double,
        longitude: Double
    ): io.reactivex.Observable<NextPrayerTimeModel> {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }
        val year = calendar.get(Calendar.YEAR)
        val rawMonth = calendar.get(Calendar.MONTH)
        val month = rawMonth + 1
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = sdf.format(calendar.time)
        return aladhanDatasourceRepository.getPrayersTimeV2(
            year = year,
            month = month,
            latitude = latitude,
            longitude = longitude,
        ).map { response ->
            val indexNextPrayerTime =
                response.indexOfFirst { element -> element.date?.gregorian?.date == formattedDate }
            val nextPrayerTime = response[indexNextPrayerTime]
            NextPrayerTimeModel(
                fajr = NextPrayerTimeModel.Content(
                    date = formattedDate,
                    time = "${nextPrayerTime.timings?.fajr?.take(5)}",
                    dateTime =  "$formattedDate ${nextPrayerTime.timings?.fajr?.take(5)}"
                ),
                dhuhr = NextPrayerTimeModel.Content(
                    date = formattedDate,
                    time = "${nextPrayerTime.timings?.dhuhr?.take(5)}",
                    dateTime =  "$formattedDate ${nextPrayerTime.timings?.dhuhr?.take(5)}"
                ),
                asr = NextPrayerTimeModel.Content(
                    date = formattedDate,
                    time = "${nextPrayerTime.timings?.asr?.take(5)}",
                    dateTime =  "$formattedDate ${nextPrayerTime.timings?.asr?.take(5)}"
                ),
                maghrib = NextPrayerTimeModel.Content(
                    date = formattedDate,
                    time = "${nextPrayerTime.timings?.maghrib?.take(5)}",
                    dateTime =  "$formattedDate ${nextPrayerTime.timings?.maghrib?.take(5)}"
                ),
                isha = NextPrayerTimeModel.Content(
                    date = formattedDate,
                    time = "${nextPrayerTime.timings?.isha?.take(5)}",
                    dateTime =  "$formattedDate ${nextPrayerTime.timings?.isha?.take(5)}"
                )
            )
        }
    }

    override fun saveAlarmPrayerTime(
        isFajrAdhanActive: Boolean?,
        isDhuhrAdhanActive: Boolean?,
        isAsrAdhanActive: Boolean?,
        isMaghribAdhanActive: Boolean?,
        isIshaAdhanActive: Boolean?
    ): Observable<AlarmPrayerTimeEntityModel> {
        return appLocalDatasource.saveAlarmPrayerTime(
            isFajrAdhanActive = isFajrAdhanActive,
            isDhuhrAdhanActive = isDhuhrAdhanActive,
            isAsrAdhanActive = isAsrAdhanActive,
            isMaghribAdhanActive = isMaghribAdhanActive,
            isIshaAdhanActive = isIshaAdhanActive
        ).flatMap {
            return@flatMap appLocalDatasource.getAlarmPrayerTime()
        }
    }
}