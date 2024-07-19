package co.id.fadlurahmanf.mediaislam.core.network.others

import android.content.Context
import co.id.fadlurahmanf.mediaislam.BuildConfig
import co.id.fadlurahmanf.mediaislam.core.network.api.AladhanAPI
import co.id.fadlurahmanf.mediaislam.core.network.api.ArtikeIslamAPI
import co.id.fadlurahmanf.mediaislam.core.network.api.EQuranAPI
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkUtilities {

    fun getChuckerInterceptor(context: Context): ChuckerInterceptor.Builder {
        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_DAY
        )

        return ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(Long.MAX_VALUE)
            .alwaysReadResponseBody(true)
            .createShortcut(false)
    }

    fun okHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }

    fun createEQuranNetwork(okHttpClient: OkHttpClient): EQuranAPI {
        return Retrofit.Builder().baseUrl(BuildConfig.EQURAN_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EQuranAPI::class.java)
    }

    fun createAladhanNetwork(okHttpClient: OkHttpClient): AladhanAPI {
        return Retrofit.Builder().baseUrl(BuildConfig.ALADHAN_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AladhanAPI::class.java)
    }

    fun createArtikelIslamNetwork(okHttpClient: OkHttpClient): ArtikeIslamAPI {
        return Retrofit.Builder().baseUrl(BuildConfig.ARTIKEL_ISLAM_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArtikeIslamAPI::class.java)
    }
}