package co.id.fadlurahmanf.mediaislam.core.di.modules

import co.id.fadlurahmanf.mediaislam.article.data.datasources.ArticleDatasourceRepository
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.ArticleUseCase
import co.id.fadlurahmanf.mediaislam.core.domain.usecase.ArticleUseCaseImpl
import co.id.fadlurahmanf.mediaislam.main.data.datasources.AladhanDatasourceRepository
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.PrayerTimeUseCase
import co.id.fadlurahmanf.mediaislam.main.domain.usecase.PrayerTimeUseCaseImpl
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepository
import co.id.fadlurahmanf.mediaislam.quran.data.repository.QuranNotificationRepository
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCase
import co.id.fadlurahmanf.mediaislam.quran.domain.usecase.QuranUseCaseImpl
import co.id.fadlurahmanfdev.kotlin_core_platform.data.repository.CorePlatformLocationRepository
import dagger.Module
import dagger.Provides

@Module
class SharedUseCaseModule {

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
    fun provideArticleUseCase(
        articleDatasourceRepository: ArticleDatasourceRepository,
    ): ArticleUseCase {
        return ArticleUseCaseImpl(
            articleDatasourceRepository = articleDatasourceRepository
        )
    }
}