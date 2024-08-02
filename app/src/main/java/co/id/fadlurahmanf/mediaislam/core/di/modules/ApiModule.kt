package co.id.fadlurahmanf.mediaislam.core.di.modules

import android.content.Context
import co.id.fadlurahmanf.mediaislam.BuildConfig
import co.id.fadlurahmanf.mediaislam.core.network.api.AladhanAPI
import co.id.fadlurahmanf.mediaislam.core.network.api.ArtikeIslamAPI
import co.id.fadlurahmanf.mediaislam.core.network.api.EQuranAPI
import co.id.fadlurahmanf.mediaislam.core.network.interceptor.OfflineCacheInterceptor
import co.id.fadlurahmanf.mediaislam.core.network.other.NetworkUtility
import co.id.fadlurahmanfdev.kotlin_feature_network.data.repository.FeatureNetworkRepository
import co.id.fadlurahmanfdev.kotlin_feature_network.data.repository.FeatureNetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Cache
import java.io.File

@Module
class ApiModule {

    @Provides
    fun provideFeatureNetworkRepository(): FeatureNetworkRepository {
        return FeatureNetworkRepositoryImpl()
    }

    @Provides
    fun provideEQuranNetwork(
        context: Context,
        featureNetworkRepository: FeatureNetworkRepository
    ): EQuranAPI {
        return featureNetworkRepository.createAPI(
            baseUrl = BuildConfig.EQURAN_BASE_URL,
            callAdapterFactory = RxJava3CallAdapterFactory.create(),
            okHttpClient = featureNetworkRepository.getOkHttpClientBuilder(
                useLoggingInterceptor = BuildConfig.FLAVOR == "dev"
            ).apply {
                val cacheFile = File(context.cacheDir, "http-cache")
                cache(Cache(cacheFile, 10L * 1024L * 1024L))
                addInterceptor(OfflineCacheInterceptor())
                if (BuildConfig.FLAVOR == "dev") {
                    addInterceptor(
                        featureNetworkRepository.getChuckerInterceptorBuilder(context).build()
                    )
                }
            }.build(),
            clazz = EQuranAPI::class.java,
        )
    }

    @Provides
    fun provideAladhanNetwork(
        context: Context,
    ): AladhanAPI {
        return NetworkUtility.provideAladhanNetwork(
            context,
            callAdapterFactory = RxJava3CallAdapterFactory.create()
        )
    }

    @Provides
    fun provideArtikelIslamNetwork(
        context: Context,
        featureNetworkRepository: FeatureNetworkRepository,
    ): ArtikeIslamAPI {
        return featureNetworkRepository.createAPI(
            baseUrl = BuildConfig.ARTIKEL_ISLAM_BASE_URL,
            callAdapterFactory = RxJava3CallAdapterFactory.create(),
            okHttpClient = featureNetworkRepository.getOkHttpClientBuilder(
                useLoggingInterceptor = BuildConfig.FLAVOR == "dev"
            ).apply {
                if (BuildConfig.FLAVOR == "dev") {
                    addInterceptor(
                        featureNetworkRepository.getChuckerInterceptorBuilder(context).build()
                    )
                }
            }.build(),
            clazz = ArtikeIslamAPI::class.java,
        )
    }
}