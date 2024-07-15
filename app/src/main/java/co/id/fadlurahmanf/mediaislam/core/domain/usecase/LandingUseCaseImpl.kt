package co.id.fadlurahmanf.mediaislam.core.domain.usecase

import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import io.reactivex.rxjava3.core.Observable

class LandingUseCaseImpl(
    private val appLocalDatasource: AppLocalDatasource,
    private val corePlatformLocationRepository: CorePlatformLocationRepository,
) : LandingUseCase {
    override fun canGoToMainPage(): Observable<Boolean> {
        return appLocalDatasource.getIsFirstInstall().map { firstInstall ->
            !firstInstall
        }
    }

    override fun saveIsNotFirstInstall(): Observable<Unit> {
        return appLocalDatasource.saveIsNotFirstInstall()
    }
}