package com.bendaniel10.reservations.viewmodel

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.bendaniel10.reservations.RxImmediateSchedulerRule
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.database.entity.RestaurantTable
import com.bendaniel10.reservations.repository.RestaurantTableListRepository
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RestaurantTableListViewModelTest {

    private lateinit var appDatabase: AppDatabase

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    private lateinit var restaurantTableListRepository: RestaurantTableListRepository

    private lateinit var viewModel: RestaurantTableListViewModel

    @Before
    fun setUp() {

        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).allowMainThreadQueries().build()

        this.restaurantTableListRepository = Mockito.mock(RestaurantTableListRepository::class.java)

        this.viewModel = RestaurantTableListViewModel(restaurantTableListRepository, appDatabase)

    }

    @After
    fun tearDown() {

        appDatabase.close()

    }

    @Test
    fun getSelectedRestaurantTable() {

        Assert.assertNotNull("Must not be null", viewModel.selectedRestaurantTable)

    }

    @Test
    fun getRestaurantTablePagedListLiveData() {

        Mockito.verify(restaurantTableListRepository).getRestaurantTablePagedList()

    }

    @Test
    fun getReservationDao() {

        Assert.assertNotNull("Must not be null", viewModel.reservationDao)

    }

    @Test
    fun networkStateLiveDataNotNull() {

        Assert.assertNotNull("Must not be null", viewModel.networkStateLiveData)

    }

    @Test
    fun reserveTableForCustomer() {

        val restaurantTable = RestaurantTable(true).also {

            it.id = 1

        }

        val customer = Customer(1, "Foo", "Bar")

        viewModel.reserveTableForCustomer(restaurantTable, customer)

        Mockito.verify(restaurantTableListRepository).reserveTableForCustomer(restaurantTable, customer)

    }
}