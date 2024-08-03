package co.id.fadlurahmanf.mediaislam.alarm

import co.id.fadlurahmanf.mediaislam.alarm.domain.usecase.AlarmUseCase
import co.id.fadlurahmanf.mediaislam.alarm.domain.usecase.AlarmUseCaseImpl
import co.id.fadlurahmanf.mediaislam.main.data.datasources.AladhanDatasourceRepository
import co.id.fadlurahmanf.mediaislam.storage.data.datasource.AppLocalDatasource
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import dagger.Module
import dagger.Provides

@Module(subcomponents = [AlarmSubComponent::class])
class AlarmModule {
    @Provides
    fun provideAlarmUseCase(
        appLocalDatasource: AppLocalDatasource,
        corePlatformLocationRepository: CorePlatformLocationRepository,
        aladhanDatasourceRepository: AladhanDatasourceRepository
    ): AlarmUseCase {
        return AlarmUseCaseImpl(
            appLocalDatasource = appLocalDatasource,
            corePlatformLocationRepository = corePlatformLocationRepository,
            aladhanDatasourceRepository = aladhanDatasourceRepository,
        )
    }
}