package co.id.fadlurahmanf.mediaislam.main.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.id.fadlurahmanf.mediaislam.article.domain.usecase.ArticleUseCase
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import co.id.fadlurahmanf.mediaislam.core.network.exception.fromEQuranException
import co.id.fadlurahmanf.mediaislam.core.ui.BaseViewModel
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.PrayerTimeUseCase
import co.id.fadlurahmanf.mediaislam.core.state.AladhanNetworkState
import co.id.fadlurahmanf.mediaislam.core.state.ArticleNetworkState
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val quranUseCase: QuranUseCase,
    private val prayerTimeUseCase: PrayerTimeUseCase,
    private val articleUseCase: ArticleUseCase,
) : BaseViewModel() {

    private val _progressBarVisible = MutableLiveData<Boolean>(false)
    val progressBarVisible: LiveData<Boolean> = _progressBarVisible

    private fun setProgressBar() {
        _progressBarVisible.value =
            listSurahLive.value is EQuranNetworkState.LOADING || prayersTimeLive.value is AladhanNetworkState.LOADING || articleNetworkLive.value is ArticleNetworkState.LOADING
    }

    private val _listSurahLive = MutableLiveData<EQuranNetworkState<List<SurahModel>>>()
    val listSurahLive: LiveData<EQuranNetworkState<List<SurahModel>>> = _listSurahLive

    fun getFirst10Surah() {
        _progressBarVisible.value = true
        _listSurahLive.value = EQuranNetworkState.LOADING
        baseDisposable.add(quranUseCase.getListSurah()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _listSurahLive.value = EQuranNetworkState.SUCCESS(
                        data = it.take(10)
                    )
                },
                {
                    _listSurahLive.value = EQuranNetworkState.ERROR(it.fromEQuranException())
                },
                {
                    setProgressBar()
                }
            ))
    }

    private val _prayersTimeLive =
        MutableLiveData<AladhanNetworkState<PrayersTimeModel>>(AladhanNetworkState.IDLE)
    val prayersTimeLive: LiveData<AladhanNetworkState<PrayersTimeModel>> = _prayersTimeLive

    fun getPrayerTime(context: Context) {
        if (_prayersTimeLive.value == AladhanNetworkState.IDLE) {
            _prayersTimeLive.value = AladhanNetworkState.LOADING
        }
        prayerTimeUseCase.getAddress()
    }

    private val _articleNetworkLive =
        MutableLiveData<ArticleNetworkState<List<ArticleItemResponse>>>(ArticleNetworkState.IDLE)
    val articleNetworkLive: LiveData<ArticleNetworkState<List<ArticleItemResponse>>> =
        _articleNetworkLive

    fun getTop3Article() {
        _articleNetworkLive.value = ArticleNetworkState.LOADING
        baseDisposable.add(articleUseCase.getTop3Article()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _articleNetworkLive.value = ArticleNetworkState.SUCCESS(
                        data = it
                    )
                },
                {
//                    _articleNetworkLive.value = EQuranNetworkState.ERROR(it.fromEQuranException())
                },
                {}
            ))
    }
}