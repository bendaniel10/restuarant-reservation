package com.bendaniel10.reservations.network.impl

import com.bendaniel10.reservations.network.WebServiceBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class WebServiceBuilderImpl(private val converterFactory: retrofit2.Converter.Factory, internal var client: OkHttpClient) : WebServiceBuilder {

    internal var endPoint: String? = null
    internal var portNumber: String? = null

    override fun clearInterceptors(): WebServiceBuilder {

        val clientBuilder = client.newBuilder()

        clientBuilder.interceptors().clear()
        client = clientBuilder.build()

        return this
    }

    override fun addInterceptor(interceptor: Interceptor): WebServiceBuilder {
        client = client.newBuilder().addInterceptor(interceptor).build()
        return this
    }


    override fun addInterceptors(vararg interceptorArray: Interceptor): WebServiceBuilder {
        for (i in interceptorArray) {
            addInterceptor(i)
        }
        return this
    }

    override fun withEndpoint(endpoint: String): WebServiceBuilder {
        this.endPoint = endpoint
        return this
    }


    override fun withPort(port: String?): WebServiceBuilder {
        this.portNumber = port
        return this
    }


    override fun build(clazz: Class<*>, logNetworkRequests: Boolean): Any {

        if (logNetworkRequests) {

            enableNetworkRequestLogging()

        }

        configureEndpointWithPort()

        return Retrofit.Builder()
                .baseUrl(endPoint!!)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build().create(clazz)

    }

    private fun enableNetworkRequestLogging() {

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(logInterceptor)

    }

    private fun configureEndpointWithPort() {
        if (portNumber != null) {

            if (endPoint != null && endPoint!!.endsWith("/")) {

                endPoint = endPoint?.removeSuffix("/")

            }

            endPoint = endPoint?.plus(":").plus(portNumber).plus("/")

        }
    }


}