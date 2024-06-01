package co.id.fadlurahmanf.mediaislam.core.di.modules

import android.content.Context
import co.id.fadlurahmanf.mediaislam.BaseApp
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.PermissionUseCase
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.PermissionUseCaseImpl
import co.id.fadlurahmanf.mediaislam.core.network.api.AladhanAPI
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
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformRepository
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class SharedModule {

    @Provides
    fun provideCorePlatformRepository(): CorePlatformRepository {
        return CorePlatformRepositoryImpl()
    }

    @Provides
    fun providePermissionUseCase(corePlatformRepository: CorePlatformRepository): PermissionUseCase {
        return PermissionUseCaseImpl(corePlatformRepository)
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
        corePlatformRepository: CorePlatformRepository
    ): PrayerTimeUseCase {
        return PrayerTimeUseCaseImpl(
            aladhanDatasourceRepository = aladhanDatasourceRepository,
            corePlatformRepository = corePlatformRepository
        )
    }
}