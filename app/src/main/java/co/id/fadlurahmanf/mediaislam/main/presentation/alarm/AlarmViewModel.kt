package co.id.fadlurahmanf.mediaislam.main.presentation.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.id.fadlurahmanf.mediaislam.core.state.AladhanNetworkState
import co.id.fadlurahmanf.mediaislam.core.ui.BaseViewModel
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.PrayerTimeUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AlarmViewModel @Inject constructor(
    private val prayerTimeUseCase: PrayerTimeUseCase
) : BaseViewModel() {

    private val _prayersTimeLive =
        MutableLiveData<AladhanNetworkState<PrayersTimeModel>>(AladhanNetworkState.IDLE)
    val prayersTimeLive: LiveData<AladhanNetworkState<PrayersTimeModel>> = _prayersTimeLive

    fun getTodayPrayerTime() {
        if (_prayersTimeLive.value == AladhanNetworkState.IDLE) {
            _prayersTimeLive.value = AladhanNetworkState.LOADING
        }
        baseDisposable.add(prayerTimeUseCase.getTodayPrayerTimeByAddress()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _prayersTimeLive.value = AladhanNetworkState.SUCCESS(data = it)
                },
                {
                    _prayersTimeLive.value = AladhanNetworkState.IDLE
                },
                {}
            ))
    }
}