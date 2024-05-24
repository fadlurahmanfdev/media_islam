package co.id.fadlurahmanf.mediaislam.main.domain.usecase

import android.content.Context
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import co.id.fadlurahmanfdev.kotlin_core_platform.data.exception.CorePlatformException
import co.id.fadlurahmanfdev.kotlin_core_platform.data.model.AddressModel
import io.reactivex.rxjava3.core.Observable

interface PrayerTimeUseCase {
    fun getAddress(
        context: Context,
        onSuccessGetAddress: (AddressModel) -> Unit,
        onError: (CorePlatformException) -> Unit
    )
    fun getCurrentPrayerTime(context: Context, address: AddressModel): Observable<PrayersTimeModel>
}