package co.id.fadlurahmanf.mediaislam.quran

import co.id.fadlurahmanf.mediaislam.core.di.modules.SharedModule
import co.id.fadlurahmanf.mediaislam.core.network.api.EQuranAPI
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepository
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepositoryImpl
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCaseImpl
import dagger.Module
import dagger.Provides

@Module(subcomponents = [QuranSubComponent::class], includes = [SharedModule::class])
class QuranModule {}