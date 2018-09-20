package com.bendaniel10.reservations.repository.impl

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.database.entity.Reservation
import com.bendaniel10.reservations.database.entity.RestaurantTable
import com.bendaniel10.reservations.network.service.ApiWebService
import com.bendaniel10.reservations.repository.RestaurantTableListRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RestaurantTableListRepositoryImpl(private val apiWebService: ApiWebService, private val appDatabase: AppDatabase) : RestaurantTableListRepository {


    override fun loadTableReservationsToCache() {

        apiWebService.fetchTablesJSONFile()
                .subscribeOn(Schedulers.io())
                .flatMap {

                    Observable.just(it.string())

                }.subscribe(

                        {

                            val restaurantDao = appDatabase.restaurantDao()

                            val listType = object : TypeToken<ArrayList<Boolean>>() {}.type

                            val tableReservationJSONList = Gson().fromJson<MutableList<Boolean>>(it, listType)

                            tableReservationJSONList.map { isAvailable ->

                                RestaurantTable(isAvailable)

                            }.forEach { restaurant ->

                                restaurantDao.insertAll(restaurant)

                            }

                        },

                        { Timber.e(it) })

    }

    override fun reserveTableForCustomer(restaurantTable: RestaurantTable, currentCustomer: Customer) {

        val reservationDao = appDatabase.reservationDao()

        val reservation = Reservation(currentCustomer.id, restaurantTable.id)

        reservationDao.insertAll(reservation)

    }

    override fun getRestaurantTablePagedList(): LiveData<PagedList<RestaurantTable>> {

        val dataSourceFactory = appDatabase.restaurantDao().fetchRestaurantTablePaged()

        val pagedListBuilder: LivePagedListBuilder<Int, RestaurantTable> = LivePagedListBuilder<Int, RestaurantTable>(dataSourceFactory,
                50)

        return pagedListBuilder.build()

    }

}