package co.id.fadlurahmanf.mediaislam.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.id.fadlurahmanf.mediaislam.core.network.exception.EQuranException
import co.id.fadlurahmanf.mediaislam.core.network.exception.fromEQuranException
import co.id.fadlurahmanf.mediaislam.core.ui.BaseViewModel
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.DetailSurahModel
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val quranUseCase: QuranUseCase
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

    private val _detailSurahLive = MutableLiveData<EQuranNetworkState<DetailSurahModel>>()
    val detailSurahLive: LiveData<EQuranNetworkState<DetailSurahModel>> = _detailSurahLive

    fun getDetailSurah(surahNo: Int) {
        _detailSurahLive.value = EQuranNetworkState.LOADING
        baseDisposable.add(quranUseCase.getDetailSurah(surahNo).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data ->
                    _detailSurahLive.value = EQuranNetworkState.SUCCESS(data = data)
                },
                { error ->
                    _detailSurahLive.value = EQuranNetworkState.ERROR(error.fromEQuranException())
                },
                {}
            ))
    }
}