package com.bendaniel10.reservations.di.module

import com.bendaniel10.reservations.app.Constants
import com.bendaniel10.reservations.di.scopes.ApplicationScope
import com.bendaniel10.reservations.network.WebServiceBuilder
import com.bendaniel10.reservations.network.WebServiceFactory
import com.bendaniel10.reservations.network.impl.WebServiceBuilderImpl
import com.bendaniel10.reservations.network.impl.WebServiceFactoryImpl
import com.bendaniel10.reservations.network.service.ApiWebService
import com.bendaniel10.reservations.network.service.impl.ApiWebServiceImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideConverterFactory(): retrofit2.Converter.Factory {

        return GsonConverterFactory.create(Gson())

    }

    @Provides
    @ApplicationScope
    fun provideOkHttpClient(): OkHttpClient {

        var okHttpClient = OkHttpClient()
        okHttpClient = okHttpClient.newBuilder()
                .connectTimeout(Constants.REST_SOCKET_TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .readTimeout(Constants.REST_SOCKET_TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
                .build()

        okHttpClient.dispatcher().maxRequestsPerHost = Constants.MAX_REQUESTS_PER_HOST

        return okHttpClient
    }

    @Provides
    @ApplicationScope
    fun provideWebServiceBuilder(converterFactory: Converter.Factory, client: OkHttpClient): WebServiceBuilder = WebServiceBuilderImpl(converterFactory, client)


    @Provides
    @ApplicationScope
    fun provideWebServiceFactory(webServiceBuilder: WebServiceBuilder): WebServiceFactory = WebServiceFactoryImpl(webServiceBuilder)

    @Provides
    @ApplicationScope
    fun provideApiWebService(webServiceFactory: WebServiceFactory): ApiWebService = ApiWebServiceImpl(webServiceFactory)

}