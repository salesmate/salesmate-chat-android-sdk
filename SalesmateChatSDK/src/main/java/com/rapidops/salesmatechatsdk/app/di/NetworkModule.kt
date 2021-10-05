package com.rapidops.salesmatechatsdk.app.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.rapidops.salesmatechatsdk.BuildConfig
import com.rapidops.salesmatechatsdk.app.coroutines.CoroutineContextProvider
import com.rapidops.salesmatechatsdk.app.coroutines.ICoroutineContextProvider
import com.rapidops.salesmatechatsdk.data.interceptor.RequestInterceptor
import com.rapidops.salesmatechatsdk.data.utils.GsonUtils
import com.rapidops.salesmatechatsdk.data.webserivce.IService
import com.rapidops.salesmatechatsdk.domain.datasources.IAppSettingsDataSource
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


@Module
internal class NetworkModule(
    private val baseUrl: String,
    private val debuggable: Boolean
) {

    companion object {
        private const val CONNECTION_TIMEOUT_SEC = 30
        private const val WRITE_TIMEOUT_SEC = 120
        private const val READ_TIMEOUT_SEC = 30
    }

    @Provides
    @ApplicationScope
    fun provideOKHttpClient(interceptorList: MutableList<Interceptor>): OkHttpClient {
        var builder = OkHttpClient.Builder()
        builder.interceptors().addAll(interceptorList)
        if (debuggable) {
            builder = builder.addNetworkInterceptor(StethoInterceptor())
        }
        builder = builder.connectTimeout(CONNECTION_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
        builder = builder.writeTimeout(WRITE_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
        builder = builder.readTimeout(READ_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    @Provides
    @ApplicationScope
    fun provideGson() = GsonUtils.gson

    @Provides
    @ApplicationScope
    fun providesService(okHttpClient: OkHttpClient, gson: Gson): IService {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(IService::class.java)

    }

    @Provides
    @ApplicationScope
    fun provideRequestInterceptor(
        appSettingsDataSource: IAppSettingsDataSource
    ): RequestInterceptor {
        return RequestInterceptor(appSettingsDataSource)
    }

    @Provides
    @ApplicationScope
    fun provideCoroutineContextProvider(): ICoroutineContextProvider {
        return CoroutineContextProvider()
    }

    @Provides
    @ApplicationScope
    fun getInterceptorList(requestInterceptor: RequestInterceptor): MutableList<Interceptor> {
        val interceptorList = ArrayList<Interceptor>()
        interceptorList.add(requestInterceptor)
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        interceptorList.add(loggingInterceptor)
        return interceptorList
    }
}
