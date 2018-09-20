package com.bendaniel10.reservations.viewmodel

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.repository.CustomersListRepository
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CustomerListViewModelTest {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    private lateinit var appDatabase: AppDatabase

    private lateinit var customersListRepository: CustomersListRepository

    private lateinit var viewModel: CustomerListViewModel

    @Before
    fun setUp() {

        appDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).build()

        this.customersListRepository = Mockito.mock(CustomersListRepository::class.java)

        this.viewModel = CustomerListViewModel(customersListRepository, appDatabase)

    }

    @After
    fun tearDown() {

        appDatabase.close()

    }

    @Test

    fun getReservationDao() {

        Assert.assertNotNull("Must not be null", viewModel.reservationDao)

    }

    @Test
    fun getSelectedCustomer() {

        Assert.assertNotNull("Must not be null", viewModel.selectedCustomer)

    }

    @Test
    fun getCustomersPagedListLiveData() {

        Mockito.verify(customersListRepository).getCustomersPagedList()

    }


    @Test
    fun networkStateLiveDataNotNull() {

        Assert.assertNotNull("Must not be null", viewModel.networkStateLiveData)

    }
}