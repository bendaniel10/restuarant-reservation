package com.bendaniel10.reservations.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.bendaniel10.reservations.database.entity.Customer

interface CustomersListRepository {

    fun loadCustomersToCache()
    fun getCustomersPagedList(): LiveData<PagedList<Customer>>
}