package com.bendaniel10.reservations.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.database.entity.RestaurantTable
import com.bendaniel10.reservations.repository.RestaurantTableListRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RestaurantTableListViewModel(private val restaurantTableListRepository: RestaurantTableListRepository,
                                   private val appDatabase: AppDatabase) : ViewModel() {

    init {

        loadRestaurantsToCacheIfEmpty()

    }

    private val compositeDisposable = CompositeDisposable()

    val selectedRestaurantTable = MutableLiveData<Pair<Int, RestaurantTable>>()

    val restaurantTablePagedListLiveData = restaurantTableListRepository.getRestaurantTablePagedList()

    val reservationDao = appDatabase.reservationDao()

    private fun loadRestaurantsToCacheIfEmpty() {

        Single.fromCallable<Boolean> {

            val restaurantTableDao = appDatabase.restaurantDao()

            val cacheEmpty = restaurantTableDao.countAll() == 0

            cacheEmpty

        }.subscribeOn(Schedulers.io())
                .subscribe(
                        { cacheEmpty ->

                            if (cacheEmpty) {

                                restaurantTableListRepository.loadTableReservationsToCache()

                            }

                        },
                        {
                            Timber.e(it)
                        }
                )

    }

    fun reserveTableForCustomer(restaurantTable: RestaurantTable, currentCustomer: Customer) {

        Completable.fromCallable {

            restaurantTableListRepository.reserveTableForCustomer(restaurantTable, currentCustomer)

        }.subscribeOn(Schedulers.io())
                .doOnError { Timber.e(it) }
                .subscribe {}

    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()

    }

}