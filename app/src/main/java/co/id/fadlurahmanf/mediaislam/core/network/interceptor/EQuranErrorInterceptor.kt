package co.id.fadlurahmanf.mediaislam.core.network.interceptor

import android.util.Log
import co.id.fadlurahmanf.mediaislam.core.constant.AppConstant
import co.id.fadlurahmanf.mediaislam.core.network.exception.EQuranException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

class EQuranErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            return chain.proceed(request)
        } catch (e: Throwable) {
            Log.d(EQuranErrorInterceptor::class.java.simpleName, "error message: ${e.message}")
            Log.d(EQuranErrorInterceptor::class.java.simpleName, "error cause: ${e.cause}")
            Log.d(
                EQuranErrorInterceptor::class.java.simpleName,
                "error localizedMessage: ${e.localizedMessage}"
            )
            Log.d(
                EQuranErrorInterceptor::class.java.simpleName,
                "error java class: ${e.javaClass.simpleName}"
            )
            if (e is UnknownHostException) {
                throw EQuranException(
                    enumCode = AppConstant.UNKNOWN_HOST_EXCEPTION_CODE,
                    message = e.localizedMessage ?: "-"
                )
            }
            throw EQuranException(
                enumCode = "GENERAL",
                message = e.message ?: "-",
            )
        }
    }
}