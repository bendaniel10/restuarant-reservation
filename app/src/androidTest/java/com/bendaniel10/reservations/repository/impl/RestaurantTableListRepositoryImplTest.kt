package com.bendaniel10.reservations.repository.impl

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.bendaniel10.reservations.RxImmediateSchedulerRule
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.database.dao.RestaurantTableDao
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.database.entity.RestaurantTable
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


class RestaurantTableListRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase

    private lateinit var apiWebService: ApiWebService

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    private lateinit var repository: RestaurantTableListRepositoryImpl

    private lateinit var restuarantTableJSON: String

    @Before
    fun setUp() {

        restuarantTableJSON = "[false,false,false,true,false,false,true,false,false,false]"

        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).allowMainThreadQueries().build()

        this.apiWebService = Mockito.mock(ApiWebService::class.java)

        Mockito.`when`(apiWebService.fetchTablesJSONFile()).thenReturn(Observable.just(ResponseBody.create(MediaType.get("text/plain"), restuarantTableJSON)))

        this.repository = RestaurantTableListRepositoryImpl(apiWebService, appDatabase)

    }

    @Test
    fun loadTableReservationsToCache() {

        repository.loadTableReservationsToCache()

        Mockito.verify(apiWebService).fetchTablesJSONFile()

        Assert.assertTrue("After fetching restaurant tables from the webservice they must be stored in the cache.", appDatabase.restaurantDao().countAll() > 0)

    }

    @Test
    fun testThatReserveTableForCustomerIncreasesReservationCount() {

        val reservationDao = appDatabase.reservationDao()
        val customerDao = appDatabase.customerDao()
        val restaurantDao = appDatabase.restaurantDao()

        val customerId = 1

        val countBeforeReservation = reservationDao.countReservationByCustomer(customerId)

        val table = RestaurantTable(true)
        val tableId = restaurantDao.insertAll(table)[0]
        table.id = tableId.toInt()

        val anyCustomer = Customer(customerId, "Foo", "Bar")
        customerDao.insertAll(anyCustomer)

        repository.reserveTableForCustomer(table, anyCustomer)

        val countAfterReservation = reservationDao.countReservationByCustomer(customerId)

        Assert.assertTrue("Reservations count must increase after a reservation", countBeforeReservation < countAfterReservation)

    }

    @Test
    fun getRestaurantTablePagedList() {

        val spyDatabase = Mockito.spy(appDatabase)
        val mockRestaurantTableDao = Mockito.mock(RestaurantTableDao::class.java)

        Mockito.`when`(spyDatabase.restaurantDao()).thenReturn(mockRestaurantTableDao)
        Mockito.`when`(mockRestaurantTableDao.fetchRestaurantTablePaged()).thenReturn(appDatabase.restaurantDao().fetchRestaurantTablePaged())

        repository = RestaurantTableListRepositoryImpl(apiWebService, spyDatabase)

        Assert.assertNotNull("Must not be null", repository.getRestaurantTablePagedList())

        Mockito.verify(spyDatabase.restaurantDao()).fetchRestaurantTablePaged()

    }
}