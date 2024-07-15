package co.id.fadlurahmanf.mediaislam.main.presentation.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.LandingUseCase
import co.id.fadlurahmanf.mediaislam.core.state.AppState
import co.id.fadlurahmanf.mediaislam.core.ui.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val landingUseCase: LandingUseCase
) : BaseViewModel() {

    private val _canGoToMainPageLive = MutableLiveData<AppState<Boolean>>()
    val canGoToMainPageLive: LiveData<AppState<Boolean>> = _canGoToMainPageLive

    fun init() {
        _canGoToMainPageLive.value = AppState.LOADING
        baseDisposable.add(landingUseCase.canGoToMainPage()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { canGo ->
                    _canGoToMainPageLive.value = AppState.SUCCESS(canGo)
                },
                {
                    _canGoToMainPageLive.value = AppState.ERROR
                },
                {}
            ))
    }

}