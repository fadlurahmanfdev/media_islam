package co.id.fadlurahmanf.mediaislam.core.di.modules

import android.content.Context
import co.id.fadlurahmanf.mediaislam.quran.data.repository.QuranNotificationRepository
import co.id.fadlurahmanf.mediaislam.quran.data.repository.QuranNotificationRepositoryImpl
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepositoryImpl
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
    fun provideCorePlatformLocationRepository(context: Context): CorePlatformLocationRepository {
        return CorePlatformLocationRepositoryImpl(context)
    }

    @Provides
    fun provideQuranNotificationRepositoryImpl(context: Context): QuranNotificationRepository {
        return QuranNotificationRepositoryImpl(context)
    }
}