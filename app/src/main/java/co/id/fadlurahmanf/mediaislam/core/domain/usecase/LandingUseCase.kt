package co.id.fadlurahmanf.mediaislam.core.domain.usecase

import android.content.Context
import io.reactivex.rxjava3.core.Observable

interface LandingUseCase {
    fun canGoToMainPage(): Observable<Boolean>

    fun saveIsNotFirstInstall(context: Context): Observable<Unit>
}