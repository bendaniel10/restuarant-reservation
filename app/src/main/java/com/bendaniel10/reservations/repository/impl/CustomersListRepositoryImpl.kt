package com.bendaniel10.reservations.repository.impl

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.network.service.ApiWebService
import com.bendaniel10.reservations.repository.CustomersListRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class CustomersListRepositoryImpl(private val apiWebService: ApiWebService, private val appDatabase: AppDatabase) : CustomersListRepository {

    override fun loadCustomersToCache() {

        apiWebService.fetchCustomersJSONFile()
                .flatMap {

                    Observable.just(it.string())

                }.subscribe(

                        {

                            val customerDao = appDatabase.customerDao()

                            val listType = object : TypeToken<ArrayList<CustomerJSONEntry>>() {}.type

                            val customerJsonEntryList = Gson().fromJson<MutableList<CustomerJSONEntry>>(it, listType)

                            customerJsonEntryList.map { customerJsonEntry ->

                                Customer(customerJsonEntry.id, customerJsonEntry.customerFirstName, customerJsonEntry.customerLastName)

                            }.forEach { customer ->

                                customerDao.insertAll(customer)

                            }

                        },

                        { Timber.e(it) })

    }

    override fun getCustomersPagedList(): LiveData<PagedList<Customer>> {

        val customerPagedListFactory = appDatabase.customerDao().fetchCustomersPaged()

        val pagedListBuilder: LivePagedListBuilder<Int, Customer> = LivePagedListBuilder<Int, Customer>(customerPagedListFactory,
                50)

        return pagedListBuilder.build()

    }

    data class CustomerJSONEntry(val customerFirstName: String, val customerLastName: String, val id: Int)

}