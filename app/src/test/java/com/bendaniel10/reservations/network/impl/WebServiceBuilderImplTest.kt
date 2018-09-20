package com.bendaniel10.reservations.network.impl

import com.bendaniel10.reservations.network.service.ApiWebService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.converter.gson.GsonConverterFactory

class WebServiceBuilderImplTest {

    private lateinit var webServiceBuilder: WebServiceBuilderImpl

    private lateinit var converterFactory: GsonConverterFactory

    private lateinit var anyInterceptor: Interceptor

    @Before
    fun setUp() {

        this.converterFactory = GsonConverterFactory.create()
        this.webServiceBuilder = WebServiceBuilderImpl(converterFactory, OkHttpClient())
        this.anyInterceptor = Interceptor{ it.proceed(it.request()) }
    }

    @Test
    fun clearInterceptors() {

        webServiceBuilder.addInterceptor(anyInterceptor)
        webServiceBuilder.clearInterceptors()

        assertTrue("Interceptors should be 0 when clearInterceptors() is called", webServiceBuilder.client.interceptors().isEmpty())

    }

    @Test
    fun addInterceptor() {

        webServiceBuilder.clearInterceptors()
        webServiceBuilder.addInterceptor(anyInterceptor)

        assertTrue("Interceptors should be 1 after adding 1 interceptor", webServiceBuilder.client.interceptors().size == 1)

    }

    @Test
    fun addInterceptors() {

        webServiceBuilder.clearInterceptors()
        webServiceBuilder.addInterceptor(anyInterceptor)
        webServiceBuilder.addInterceptor(anyInterceptor)
        webServiceBuilder.addInterceptor(anyInterceptor)

        assertTrue("Interceptors should be 3 after adding 1 interceptor", webServiceBuilder.client.interceptors().size == 3)

    }

    @Test
    fun withEndpoint() {

        val anyEndpoint = "https://any.endpoint.com/"
        webServiceBuilder.withEndpoint(anyEndpoint)

        assertTrue("WebServiceBuilder set with method `withEndpoint()` must have set endpoint", webServiceBuilder.endPoint == anyEndpoint)

        assertTrue("Endpoint url must end with /", anyEndpoint.endsWith("/"))
    }

    @Test
    fun withPort() {

        val anyPort = "12345"
        webServiceBuilder.withPort(anyPort)

        assertTrue("WebServiceBuilder set with method `withPort()` must have set port", webServiceBuilder.portNumber == anyPort)
    }

    @Test
    fun build() {

        val anyEndpoint = "https://any.endpoint.com/"

        webServiceBuilder.withEndpoint(anyEndpoint)
        val builtService = webServiceBuilder.build(ApiWebService::class.java, true)

        assertTrue("Built service must be type of passing in service interface", builtService is ApiWebService)

    }


}