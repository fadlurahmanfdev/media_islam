package co.id.fadlurahmanf.mediaislam.core.domain.usecase

import io.reactivex.rxjava3.core.Observable

interface LandingUseCase {
    fun canGoToMainPage(): Observable<Boolean>
}