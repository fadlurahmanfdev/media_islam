package co.id.fadlurahmanf.mediaislam.core.di.modules

import android.content.Context
import co.id.fadlurahmanf.mediaislam.BaseApp
import co.id.fadlurahmanf.mediaislam.article.data.datasources.ArticleDatasourceRepository
import co.id.fadlurahmanf.mediaislam.article.data.datasources.ArticleDatasourceRepositoryImpl
import co.id.fadlurahmanf.mediaislam.core.network.api.AladhanAPI
import co.id.fadlurahmanf.mediaislam.core.network.api.ArtikeIslamAPI
import co.id.fadlurahmanf.mediaislam.core.network.api.EQuranAPI
import co.id.fadlurahmanf.mediaislam.main.data.datasources.AladhanDatasourceRepository
import co.id.fadlurahmanf.mediaislam.main.data.datasources.AladhanDatasourceRepositoryImpl
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.PrayerTimeUseCase
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.PrayerTimeUseCaseImpl
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepository
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepositoryImpl
import co.id.fadlurahmanf.mediaislam.quran.data.repository.QuranNotificationRepository
import co.id.fadlurahmanf.mediaislam.quran.data.repository.QuranNotificationRepositoryImpl
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCaseImpl
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class SharedModule {

    @Provides
    fun provideCorePlatformLocationRepository(context: Context): CorePlatformLocationRepository {
        return CorePlatformLocationRepositoryImpl(context)
    }

    @Provides
    fun provideQuranNotificationRepositoryImpl(context: Context): QuranNotificationRepository {
        return QuranNotificationRepositoryImpl(context)
    }

    @Provides
    fun provideEQuranDatasourceModule(
        context: Context,
        eQuranAPI: EQuranAPI
    ): EQuranDatasourceRepository {
        val app = context.applicationContext as BaseApp
        return EQuranDatasourceRepositoryImpl(eQuranAPI, app.firebaseRemoteConfig)
    }

    @Provides
    fun provideQuranUseCase(
        eQuranDatasourceRepository: EQuranDatasourceRepository,
        quranNotificationRepository: QuranNotificationRepository
    ): QuranUseCase {
        return QuranUseCaseImpl(
            quranRepository = eQuranDatasourceRepository,
            quranNotificationRepository = quranNotificationRepository
        )
    }

    @Provides
    fun provideAladhanDatasourceModule(aladhanAPI: AladhanAPI): AladhanDatasourceRepository {
        return AladhanDatasourceRepositoryImpl(aladhanAPI)
    }

    @Provides
    fun providePrayerTimeUseCase(
        aladhanDatasourceRepository: AladhanDatasourceRepository,
        corePlatformLocationRepository: CorePlatformLocationRepository
    ): PrayerTimeUseCase {
        return PrayerTimeUseCaseImpl(
            aladhanDatasourceRepository = aladhanDatasourceRepository,
            corePlatformLocationRepository = corePlatformLocationRepository
        )
    }

    @Provides
    fun provideArticleDatasourceRepository(
        artikeIslamAPI: ArtikeIslamAPI
    ): ArticleDatasourceRepository {
        return ArticleDatasourceRepositoryImpl(
            artikeIslamAPI = artikeIslamAPI
        )
    }
}