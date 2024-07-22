package co.id.fadlurahmanf.mediaislam.main

import android.content.Context
import co.id.fadlurahmanf.mediaislam.BaseApp
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.LandingUseCase
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.LandingUseCaseImpl
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.MenuUseCase
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.MenuUseCaseImpl
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import dagger.Module
import dagger.Provides

@Module(subcomponents = [MainSubComponent::class])
class MainModule {
    @Provides
    fun provideLandingUseCase(
        appLocalDatasource: AppLocalDatasource,
        corePlatformLocationRepository: CorePlatformLocationRepository
    ): LandingUseCase {
        return LandingUseCaseImpl(
            appLocalDatasource = appLocalDatasource,
            corePlatformLocationRepository = corePlatformLocationRepository
        )
    }

    @Provides
    fun provideMenuUseCase(
        context: Context
    ): MenuUseCase {
        return MenuUseCaseImpl(
            firebaseRemoteConfig = (context as BaseApp).firebaseRemoteConfig
        )
    }
}