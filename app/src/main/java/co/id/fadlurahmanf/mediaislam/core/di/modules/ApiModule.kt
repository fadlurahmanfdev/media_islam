package co.id.fadlurahmanf.mediaislam.core.di.modules

import android.content.Context
import co.id.fadlurahmanf.mediaislam.core.network.api.EQuranAPI
import co.id.fadlurahmanf.mediaislam.core.network.interceptor.EQuranErrorInterceptor
import co.id.fadlurahmanf.mediaislam.core.network.others.NetworkUtilities
import dagger.Module
import dagger.Provides

@Module
class ApiModule {

    private val networkUtilities = NetworkUtilities()

    @Provides
    fun provideEQuranNetwork(context: Context): EQuranAPI {
        return networkUtilities.createGuestIdentityNetwork(
            networkUtilities.okHttpClientBuilder()
                .addInterceptor(EQuranErrorInterceptor())
                .addInterceptor(networkUtilities.getChuckerInterceptor(context).build())
                .build()
        )
    }
}