package com.test.application.injection.module



import com.squareup.moshi.Moshi
import com.test.application.App
import com.test.application.BuildConfig
import com.test.application.data.http.HttpRestHelper
import com.test.application.data.http.HttpRestService
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class HttpModule(private val mBaseUrl: String) {



    @Provides
    @Singleton
    internal fun provideOkhttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
                client.addInterceptor {
                    val original: Request = it.request()
                    val originalHttpUrl = original.url()
                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("access_key", App.API_ACCESS_KEY)
                        .build()

                    val requestBuilder = original.newBuilder()
                        .url(url)
                    val request = requestBuilder.build()
                     it.proceed(request)
                }

        if (BuildConfig.DEBUG) {
            client.addInterceptor(logging)
        }

        return client.build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(mBaseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideHttpRestHelper(
        retrofit: Retrofit
    ): HttpRestHelper {
        return HttpRestHelper(retrofit.create(HttpRestService::class.java))
    }
}
