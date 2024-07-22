package co.id.fadlurahmanf.mediaislam.main.domain.usecase

import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import co.id.fadlurahmanfdev.kotlin_core_platform.data.model.AddressModel
import io.reactivex.rxjava3.core.Observable

interface PrayerTimeUseCase {
    fun getAddress():Observable<AddressModel>
    fun getCurrentPrayerTimeByAddress(address: AddressModel): Observable<PrayersTimeModel>
    fun getCurrentPrayerTimeByAddress(): Observable<PrayersTimeModel>
}