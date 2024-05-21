package co.id.fadlurahmanf.mediaislam.core.di.modules

import co.id.fadlurahmanf.mediaislam.core.network.api.EQuranAPI
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepository
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepositoryImpl
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCaseImpl
import dagger.Module
import dagger.Provides

@Module
class SharedModule {
    @Provides
    fun provideEQuranDatasourceModule(eQuranAPI: EQuranAPI): EQuranDatasourceRepository {
        return EQuranDatasourceRepositoryImpl(eQuranAPI)
    }

    @Provides
    fun provideQuranUseCase(eQuranDatasourceRepository: EQuranDatasourceRepository): QuranUseCase {
        return QuranUseCaseImpl(eQuranDatasourceRepository)
    }
}