package com.arfdevs.myproject.core.data.remote

import com.arfdevs.myproject.core.BuildConfig.BASE_URL
import com.arfdevs.myproject.core.BuildConfig.Bearer
import com.arfdevs.myproject.core.helper.NoInternetInterceptor
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient(
    val chuckerInterceptor: ChuckerInterceptor,
    val noInternetInterceptor: NoInternetInterceptor,
    val networkAdapterFactory: NetworkResponseAdapterFactory
) {

    inner class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            val newRequest = request.newBuilder().addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $Bearer").build()

            return chain.proceed(newRequest)
        }

    }

    inline fun <reified I> create(): I {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(noInternetInterceptor)
            .addInterceptor(AuthInterceptor())
            .addInterceptor(chuckerInterceptor).connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(networkAdapterFactory)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).build()

        return retrofit.create(I::class.java)
    }
}