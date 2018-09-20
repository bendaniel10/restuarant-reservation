package com.bendaniel10.reservations.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.database.entity.RestaurantTable

interface RestaurantTableListRepository {
    fun loadTableReservationsToCache()
    fun getRestaurantTablePagedList(): LiveData<PagedList<RestaurantTable>>
    fun reserveTableForCustomer(restaurantTable: RestaurantTable, currentCustomer: Customer)
}