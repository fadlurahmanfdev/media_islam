package co.id.fadlurahmanf.mediaislam.core.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class EQuranErrorInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val response = chain.proceed(request)
            return response
        }catch (e:Throwable){
            Log.d(EQuranErrorInterceptor::class.java.simpleName, "error intercept: ${e.message}")
            throw Exception()
        }
    }
}