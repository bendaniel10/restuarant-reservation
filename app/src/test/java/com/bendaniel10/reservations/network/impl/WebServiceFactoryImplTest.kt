package com.bendaniel10.reservations.network.impl

import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.converter.gson.GsonConverterFactory

class WebServiceFactoryImplTest {

    private lateinit var webServiceBuilder: WebServiceBuilderImpl

    private lateinit var webServiceFactory: WebServiceFactoryImpl

    @Before
    fun setUp() {

        this.webServiceBuilder = WebServiceBuilderImpl(GsonConverterFactory.create(), OkHttpClient())
        this.webServiceFactory = WebServiceFactoryImpl(webServiceBuilder)
    }

    @Test
    fun testThatUrlAlwaysEndsWithSlash() {


        val anyEndpoint = "http://api.endpoint.com"

        webServiceFactory.buildPublicService(anyEndpoint)

        assertEquals("Url must end with slash after the `buildPublicService()` is called", anyEndpoint.plus("/"), webServiceBuilder.endPoint)

    }

    @Test
    fun buildPublicService() {

        this.webServiceBuilder = Mockito.spy(webServiceBuilder)
        this.webServiceFactory = WebServiceFactoryImpl(webServiceBuilder)

        val spyWebServiceFactory = Mockito.spy(webServiceFactory)
        val anyEndpoint = "https://any.endpoint.com/"

        spyWebServiceFactory.buildPublicService(anyEndpoint)

        Mockito.verify(webServiceBuilder).withEndpoint(anyEndpoint)

    }

    @After
    fun tearDown() {
        Mockito.validateMockitoUsage()
    }
}