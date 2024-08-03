package co.id.fadlurahmanf.mediaislam.core.domain.usecase

import android.content.Context
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformRepository
import io.reactivex.rxjava3.core.Observable

class LandingUseCaseImpl(
    private val appLocalDatasource: AppLocalDatasource,
    private val corePlatformRepository: CorePlatformRepository,
    private val corePlatformLocationRepository: CorePlatformLocationRepository,
) : LandingUseCase {
    override fun canGoToMainPage(): Observable<Boolean> {
        return appLocalDatasource.getIsFirstInstall().map { firstInstall ->
            !firstInstall
        }
    }

    override fun saveIsNotFirstInstall(context: Context): Observable<Unit> {
        val deviceId = corePlatformRepository.getDeviceId(context)
        return appLocalDatasource.saveIsNotFirstInstall(deviceId)
    }
}