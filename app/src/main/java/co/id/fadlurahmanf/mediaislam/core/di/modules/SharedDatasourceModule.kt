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
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepository
import co.id.fadlurahmanf.mediaislam.quran.data.datasources.EQuranDatasourceRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class SharedDatasourceModule {
    @Provides
    fun provideEQuranDatasourceRepository(
        context: Context,
        eQuranAPI: EQuranAPI
    ): EQuranDatasourceRepository {
        val app = context.applicationContext as BaseApp
        return EQuranDatasourceRepositoryImpl(eQuranAPI, app.firebaseRemoteConfig)
    }



    @Provides
    fun provideAladhanDatasourceRepository(aladhanAPI: AladhanAPI): AladhanDatasourceRepository {
        return AladhanDatasourceRepositoryImpl(aladhanAPI)
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