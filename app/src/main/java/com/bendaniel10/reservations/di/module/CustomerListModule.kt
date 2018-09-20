package com.bendaniel10.reservations.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.di.scopes.ActivityScope
import com.bendaniel10.reservations.network.service.ApiWebService
import com.bendaniel10.reservations.repository.CustomersListRepository
import com.bendaniel10.reservations.repository.impl.CustomersListRepositoryImpl
import com.bendaniel10.reservations.viewmodel.CustomerListViewModel
import dagger.Module
import dagger.Provides

@Module
class CustomerListModule {

    @Provides
    @ActivityScope
    fun provideCustomersListRepository(apiWebService: ApiWebService, appDatabase: AppDatabase): CustomersListRepository = CustomersListRepositoryImpl(apiWebService, appDatabase)

    @Provides
    @ActivityScope
    fun provideCustomerListViewModelProviderFactory(customersListRepository: CustomersListRepository, appDatabase: AppDatabase): CustomerListViewModelProviderFactory {

        return CustomerListViewModelProviderFactory(customersListRepository, appDatabase)

    }

    class CustomerListViewModelProviderFactory(private val customersListRepository: CustomersListRepository, private val appDatabase: AppDatabase) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {

            return CustomerListViewModel(customersListRepository, appDatabase) as T

        }

    }

}