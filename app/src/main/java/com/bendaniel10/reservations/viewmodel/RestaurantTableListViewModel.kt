package com.bendaniel10.reservations.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.widget.Toast
import com.bendaniel10.reservations.app.NetworkState
import com.bendaniel10.reservations.app.ReservationsApplication
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.database.entity.RestaurantTable
import com.bendaniel10.reservations.repository.RestaurantTableListRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class RestaurantTableListViewModel(private val restaurantTableListRepository: RestaurantTableListRepository,
                                   private val appDatabase: AppDatabase) : ViewModel() {


    private val compositeDisposable = CompositeDisposable()

    val selectedRestaurantTable = MutableLiveData<Pair<Int, RestaurantTable>>()

    val restaurantTablePagedListLiveData = restaurantTableListRepository.getRestaurantTablePagedList()

    val reservationDao = appDatabase.reservationDao()

    var networkStateLiveData: MutableLiveData<NetworkState>

    val newReservationsLiveData = MutableLiveData<Customer>()

    init {

        this.networkStateLiveData = MutableLiveData<NetworkState>().apply { this.postValue(NetworkState.LOADED) }

        loadRestaurantsToCacheIfEmpty()

    }

    private fun loadRestaurantsToCacheIfEmpty() {

        Single.fromCallable<Boolean> {

            val restaurantTableDao = appDatabase.restaurantDao()

            val cacheEmpty = restaurantTableDao.countAll() == 0

            cacheEmpty

        }.subscribeOn(Schedulers.io())
                .subscribe(
                        { cacheEmpty ->

                            if (cacheEmpty) {

                                networkStateLiveData.postValue(NetworkState.LOADING)
                                restaurantTableListRepository.loadTableReservationsToCache()
                                networkStateLiveData.postValue(NetworkState.LOADED)

                            }

                        },
                        {
                            networkStateLiveData.postValue(NetworkState.FAILED)
                            Timber.e(it)
                        }
                )

    }

    fun reserveTableForCustomer(restaurantTable: RestaurantTable, currentCustomer: Customer) {

        Completable.fromCallable {

            restaurantTableListRepository.reserveTableForCustomer(restaurantTable, currentCustomer)

        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { Timber.e(it) }
                .subscribe { newReservationsLiveData.postValue(currentCustomer) }

    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()

    }

}