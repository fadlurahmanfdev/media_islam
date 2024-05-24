package co.id.fadlurahmanf.mediaislam.main.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.id.fadlurahmanf.mediaislam.core.network.exception.fromAladhanException
import co.id.fadlurahmanf.mediaislam.core.network.exception.fromEQuranException
import co.id.fadlurahmanf.mediaislam.core.ui.BaseViewModel
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.PrayerTimeUseCase
import co.id.fadlurahmanf.mediaislam.core.state.AladhanNetworkState
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val quranUseCase: QuranUseCase,
    private val prayerTimeUseCase: PrayerTimeUseCase,
) : BaseViewModel() {
    private val _listSurahLive = MutableLiveData<EQuranNetworkState<List<SurahModel>>>()
    val listSurah: LiveData<EQuranNetworkState<List<SurahModel>>> = _listSurahLive

    fun getListSurah() {
        _listSurahLive.value = EQuranNetworkState.LOADING
        baseDisposable.add(quranUseCase.getListSurah()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _listSurahLive.value = EQuranNetworkState.SUCCESS(
                        data = it
                    )
                },
                {
                    _listSurahLive.value = EQuranNetworkState.ERROR(it.fromEQuranException())
                },
                {}
            ))
    }

    private val _prayersTimeLive =
        MutableLiveData<AladhanNetworkState<PrayersTimeModel>>(AladhanNetworkState.IDLE)
    val prayersTimeLive: LiveData<AladhanNetworkState<PrayersTimeModel>> = _prayersTimeLive

    fun getPrayerTime(context: Context) {
        if (_prayersTimeLive.value == AladhanNetworkState.IDLE) {
            _prayersTimeLive.value = AladhanNetworkState.LOADING
        }
        prayerTimeUseCase.getAddress(
            context,
            onSuccessGetAddress = { address ->
                baseDisposable.add(prayerTimeUseCase.getCurrentPrayerTime(
                    context,
                    address = address
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { model ->
                            _prayersTimeLive.value = AladhanNetworkState.SUCCESS(model)
                        },
                        { throwable ->
                            _prayersTimeLive.value =
                                AladhanNetworkState.ERROR(throwable.fromAladhanException())
                        },
                        {}
                    ))
            },
            onError = { exception ->

            },
        )
    }
}