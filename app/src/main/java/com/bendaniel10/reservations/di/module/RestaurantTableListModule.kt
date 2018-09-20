package com.bendaniel10.reservations.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.di.scopes.ActivityScope
import com.bendaniel10.reservations.network.service.ApiWebService
import com.bendaniel10.reservations.repository.RestaurantTableListRepository
import com.bendaniel10.reservations.repository.impl.RestaurantTableListRepositoryImpl
import com.bendaniel10.reservations.viewmodel.RestaurantTableListViewModel
import dagger.Module
import dagger.Provides

@Module
class RestaurantTableListModule {

    @Provides
    @ActivityScope
    fun provideRestaurantTableListRepository(apiWebService: ApiWebService, appDatabase: AppDatabase): RestaurantTableListRepository = RestaurantTableListRepositoryImpl(apiWebService, appDatabase)

    @Provides
    @ActivityScope
    fun provideRestaurantTableListViewModelProviderFactory(restaurantTableListRepository: RestaurantTableListRepository, appDatabase: AppDatabase): RestaurantTableListViewModelProviderFactory {

        return RestaurantTableListViewModelProviderFactory(restaurantTableListRepository, appDatabase)

    }

    class RestaurantTableListViewModelProviderFactory(private val restaurantTableListRepository: RestaurantTableListRepository, private val appDatabase: AppDatabase) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            return RestaurantTableListViewModel(restaurantTableListRepository, appDatabase) as T

        }

    }

}