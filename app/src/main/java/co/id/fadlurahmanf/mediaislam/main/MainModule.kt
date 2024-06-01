package co.id.fadlurahmanf.mediaislam.main

import co.id.fadlurahmanf.mediaislam.core.di.modules.SharedModule
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.LandingUseCase
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.LandingUseCaseImpl
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import dagger.Module
import dagger.Provides

@Module(subcomponents = [MainSubComponent::class], includes = [SharedModule::class])
class MainModule {
    @Provides
    fun provideLandingUseCase(appLocalDatasource: AppLocalDatasource): LandingUseCase {
        return LandingUseCaseImpl(appLocalDatasource)
    }
}