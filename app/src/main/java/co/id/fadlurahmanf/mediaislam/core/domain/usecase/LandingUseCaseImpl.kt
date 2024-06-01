package co.id.fadlurahmanf.mediaislam.core.domain.usecase

import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import io.reactivex.rxjava3.core.Observable

class LandingUseCaseImpl(private val appLocalDatasource: AppLocalDatasource) : LandingUseCase {
    override fun canGoToMainPage(): Observable<Boolean> {
        return appLocalDatasource.getIsFirstInstall().map { firstInstall ->
            !firstInstall
        }
    }
}