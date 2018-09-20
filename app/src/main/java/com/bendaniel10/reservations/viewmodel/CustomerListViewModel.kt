package com.bendaniel10.reservations.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.bendaniel10.reservations.app.NetworkState
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.repository.CustomersListRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CustomerListViewModel(private val customersListRepository: CustomersListRepository, private val appDatabase: AppDatabase) : ViewModel() {

    init {

        loadCustomersToCacheIfEmpty()

    }

    val reservationDao = appDatabase.reservationDao()

    val selectedCustomer = MutableLiveData<Pair<Int, Customer>>()

    val customersPagedListLiveData = customersListRepository.getCustomersPagedList()

    val networkStateLiveData = MutableLiveData<NetworkState>().apply { this.postValue(NetworkState.LOADED) }


    private fun loadCustomersToCacheIfEmpty() {

        Single.fromCallable<Boolean> {

            val customerDao = appDatabase.customerDao()

            val isCustomerCacheEmpty = customerDao.countAll() == 0

            isCustomerCacheEmpty

        }.subscribeOn(Schedulers.io())
                .subscribe(
                        { customerCacheIsEmpty ->

                            if (customerCacheIsEmpty) {

                                networkStateLiveData.postValue(NetworkState.LOADING)

                                customersListRepository.loadCustomersToCache()

                                networkStateLiveData.postValue(NetworkState.LOADED)

                            }

                        },
                        {
                            Timber.e(it)
                            networkStateLiveData.postValue(NetworkState.FAILED)

                        }
                )

    }


}