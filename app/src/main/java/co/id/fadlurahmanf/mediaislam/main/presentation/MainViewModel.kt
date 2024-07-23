package co.id.fadlurahmanf.mediaislam.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.ArticleUseCase
import co.id.fadlurahmanf.mediaislam.core.network.dto.response.article.ArticleItemResponse
import co.id.fadlurahmanf.mediaislam.core.network.exception.fromEQuranException
import co.id.fadlurahmanf.mediaislam.core.ui.BaseViewModel
import co.id.fadlurahmanf.mediaislam.core.state.EQuranNetworkState
import co.id.fadlurahmanf.mediaislam.quran.data.dto.model.SurahModel
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.PrayerTimeUseCase
import co.id.fadlurahmanf.mediaislam.core.state.AladhanNetworkState
import co.id.fadlurahmanf.mediaislam.core.state.AppState
import co.id.fadlurahmanf.mediaislam.core.state.ArticleNetworkState
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.ItemMainMenuModel
import co.id.fadlurahmanf.mediaislam.main.data.dto.model.PrayersTimeModel
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.MenuUseCase
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val articleUseCase: ArticleUseCase,
    private val menuUseCase: MenuUseCase,
    private val prayerTimeUseCase: PrayerTimeUseCase,
    private val quranUseCase: QuranUseCase,
) : BaseViewModel() {

    private val _mainMenusLive = MutableLiveData<AppState<List<ItemMainMenuModel>>>()
    val mainMenusLive: LiveData<AppState<List<ItemMainMenuModel>>> = _mainMenusLive

    fun getMainMenu() {
        if (mainMenusLive.value !is AppState.SUCCESS) {
            _mainMenusLive.value = AppState.LOADING
        }
        baseDisposable.add(
            menuUseCase.getMainMenus().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _mainMenusLive.value = AppState.SUCCESS(it)
                    },
                    {
                        _mainMenusLive.value = AppState.ERROR
                    },
                    {},
                ),
        )
    }

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

    fun getPrayerTime() {
        if (_prayersTimeLive.value == AladhanNetworkState.IDLE) {
            _prayersTimeLive.value = AladhanNetworkState.LOADING
        }
        baseDisposable.add(prayerTimeUseCase.getCurrentPrayerTimeByAddress()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _prayersTimeLive.value = AladhanNetworkState.SUCCESS(data = it)
                },
                {
                    _prayersTimeLive.value = AladhanNetworkState.IDLE
                },
                {
                    setProgressBar()
                }
            ))
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
                {
                    setProgressBar()
                }
            ))
    }
}