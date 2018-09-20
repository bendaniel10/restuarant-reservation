package com.bendaniel10.reservations.network.service.impl

import com.bendaniel10.reservations.network.impl.WebServiceBuilderImpl
import com.bendaniel10.reservations.network.impl.WebServiceFactoryImpl
import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.converter.gson.GsonConverterFactory

class ApiWebServiceImplTest {

    private lateinit var webServiceBuilder: WebServiceBuilderImpl

    private lateinit var webServiceFactory: WebServiceFactoryImpl
    @Before
    fun setUp() {

        this.webServiceBuilder = WebServiceBuilderImpl(GsonConverterFactory.create(), OkHttpClient())
        this.webServiceFactory = WebServiceFactoryImpl(webServiceBuilder)

    }

    @Test
    fun fetchCustomersJSONFile() {

        val spyWebServiceFactory = Mockito.spy(webServiceFactory)
        val apiWebService = ApiWebServiceImpl(spyWebServiceFactory)

        val listUsersSingle = apiWebService.fetchCustomersJSONFile()

        assertNotNull("Must not return a null object", listUsersSingle)

        Mockito.verify(spyWebServiceFactory).buildPublicService()

    }

    @Test
    fun fetchTablesJSONFile() {

        val spyWebServiceFactory = Mockito.spy(webServiceFactory)
        val apiWebService = ApiWebServiceImpl(spyWebServiceFactory)

        val listUsersSingle = apiWebService.fetchTablesJSONFile()

        assertNotNull("Must not return a null object", listUsersSingle)

        Mockito.verify(spyWebServiceFactory).buildPublicService()

    }
}