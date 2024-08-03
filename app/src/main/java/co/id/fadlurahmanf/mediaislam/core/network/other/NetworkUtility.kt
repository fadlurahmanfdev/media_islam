package co.id.fadlurahmanf.mediaislam.core.network.other

import android.content.Context
import co.id.fadlurahmanf.mediaislam.BuildConfig
import co.id.fadlurahmanf.mediaislam.core.network.api.AladhanAPI
import co.id.fadlurahmanfdev.kotlin_feature_network.data.repository.FeatureNetworkRepository
import co.id.fadlurahmanfdev.kotlin_feature_network.data.repository.FeatureNetworkRepositoryImpl
import retrofit2.CallAdapter

object NetworkUtility {
    private val featureNetworkRepository: FeatureNetworkRepository = FeatureNetworkRepositoryImpl()

    fun provideAladhanNetwork(context: Context, callAdapterFactory:CallAdapter.Factory): AladhanAPI {
        return featureNetworkRepository.createAPI(
            baseUrl = BuildConfig.ALADHAN_BASE_URL,
            callAdapterFactory = callAdapterFactory,
            okHttpClient = featureNetworkRepository.getOkHttpClientBuilder(
                useLoggingInterceptor = BuildConfig.FLAVOR == "dev"
            ).apply {
                if (BuildConfig.FLAVOR == "dev") {
                    addInterceptor(
                        featureNetworkRepository.getChuckerInterceptorBuilder(context).build()
                    )
                }
            }.build(),
            clazz = AladhanAPI::class.java,
        )
    }
}