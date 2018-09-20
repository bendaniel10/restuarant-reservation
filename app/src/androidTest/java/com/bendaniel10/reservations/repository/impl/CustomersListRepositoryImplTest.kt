package com.bendaniel10.reservations.repository.impl

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.bendaniel10.reservations.RxImmediateSchedulerRule
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.database.dao.CustomerDao
import com.bendaniel10.reservations.network.service.ApiWebService
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class CustomersListRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase

    private lateinit var apiWebService: ApiWebService

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    private lateinit var repository: CustomersListRepositoryImpl

    private lateinit var customerJSON: String

    @Before
    fun setUp() {

        this.customerJSON = " [{\n" +
                "    \"customerFirstName\": \"Paul\",\n" +
                "    \"customerLastName\": \"McCartney\",\n" +
                "    \"id\": 17\n" +
                "  },\n" +
                "  {\n" +
                "    \"customerFirstName\": \"Plato\",\n" +
                "    \"customerLastName\": \"\",\n" +
                "    \"id\": 18\n" +
                "  },\n" +
                "  {\n" +
                "    \"customerFirstName\": \"Mikhail\",\n" +
                "    \"customerLastName\": \"Gorbachev\",\n" +
                "    \"id\": 19\n" +
                "  },\n" +
                "  {\n" +
                "    \"customerFirstName\": \"Le\",\n" +
                "    \"customerLastName\": \"Lincoln\",\n" +
                "    \"id\": 20\n" +
                "  }\n" +
                "]"

        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).allowMainThreadQueries().build()

        this.apiWebService = Mockito.mock(ApiWebService::class.java)

        Mockito.`when`(apiWebService.fetchCustomersJSONFile()).thenReturn(Observable.just(ResponseBody.create(MediaType.get("text/plain"), customerJSON)))

        this.repository = CustomersListRepositoryImpl(apiWebService, appDatabase)

    }

    @Test
    fun loadCustomersToCache() {

        repository.loadCustomersToCache()

        Mockito.verify(apiWebService).fetchCustomersJSONFile()

        Assert.assertTrue("After fetching customers from the webservice they must be stored in the cache.", appDatabase.customerDao().countAll() > 0)
    }

    @Test
    fun getCustomersPagedList() {

        val spyDatabase = Mockito.spy(appDatabase)
        val mockCustomerDao = Mockito.mock(CustomerDao::class.java)

        Mockito.`when`(spyDatabase.customerDao()).thenReturn(mockCustomerDao)
        Mockito.`when`(mockCustomerDao.fetchCustomersPaged()).thenReturn(appDatabase.customerDao().fetchCustomersPaged())

        repository = CustomersListRepositoryImpl(apiWebService, spyDatabase)

        Assert.assertNotNull("Must not be null", repository.getCustomersPagedList())

        Mockito.verify(spyDatabase.customerDao()).fetchCustomersPaged()

    }

}