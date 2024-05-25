package co.id.fadlurahmanf.mediaislam.main.domain.usecase

import android.content.Context
import co.id.fadlurahmanf.mediaislam.core.network.exception.AladhanException
import co.id.fadlurahmanf.mediaislam.main.data.datasources.AladhanDatasourceRepository
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import co.id.fadlurahmanfdev.kotlin_core_platform.data.exception.CorePlatformException
import co.id.fadlurahmanfdev.kotlin_core_platform.data.model.AddressModel
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformRepository
import io.reactivex.rxjava3.core.Observable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PrayerTimeUseCaseImpl(
    val aladhanDatasourceRepository: AladhanDatasourceRepository,
    val corePlatformRepository: CorePlatformRepository
) :
    PrayerTimeUseCase {

    override fun getAddress(
        context: Context,
        onSuccessGetAddress: (AddressModel) -> Unit,
        onError: (CorePlatformException) -> Unit
    ) {
        val isEnabled =
            corePlatformRepository.isLocationPermissionEnabled(context) && corePlatformRepository.isLocationEnabled(
                context
            )
        if (!isEnabled) {
            onError(
                CorePlatformException(
                    code = "PERMISSION_NOT_GRANTED",
                    message = "Permission & Service is not granted"
                )
            )
            return
        }
        corePlatformRepository.requestAndForgetLocation(
            context,
            onSuccess = { coordinate ->
                corePlatformRepository.getAddress(
                    context,
                    latitude = coordinate.latitude,
                    longitude = coordinate.longitude,
                    onSuccess = onSuccessGetAddress,
                    onError = onError,
                )
            },
            onError = onError,
        )
    }

    override fun getCurrentPrayerTime(
        context: Context,
        address: AddressModel
    ): Observable<PrayersTimeModel> {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val rawMonth = calendar.get(Calendar.MONTH)
        val month = rawMonth + 1
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = sdf.format(calendar.time)
        val indonesianSdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "id"))
        val indonesianFormattedDate = indonesianSdf.format(calendar.time)
        return aladhanDatasourceRepository.getPrayersTime(
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
            PrayersTimeModel(
                readableDateInHijr = "${todayPrayerTime.date?.hijri?.day ?: "-"} ${todayPrayerTime.date?.hijri?.month?.en ?: "-"} ${todayPrayerTime.date?.hijri?.year ?: "-"}",
                readableDate = indonesianFormattedDate,
                location = "${address.subAdminArea}, ${address.adminArea}",
                nextPrayerTime = "asa",
                nextPrayer = "sa",
                timing = PrayersTimeModel.Timing(
                    fajr = todayPrayerTime.timings?.fajr ?: "-",
                    dhuhr = todayPrayerTime.timings?.dhuhr ?: "-",
                    asr = todayPrayerTime.timings?.asr ?: "-",
                    maghrib = todayPrayerTime.timings?.maghrib ?: "-",
                    isha = todayPrayerTime.timings?.isha ?: "-"
                )
            )
        }
    }
}