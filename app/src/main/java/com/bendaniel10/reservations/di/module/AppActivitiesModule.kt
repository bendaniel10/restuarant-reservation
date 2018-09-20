package com.bendaniel10.reservations.di.module

import com.bendaniel10.reservations.di.scopes.ActivityScope
import com.bendaniel10.reservations.ui.CustomerListActivity
import com.bendaniel10.reservations.ui.RestaurantTableListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppActivitiesModule {

    @ContributesAndroidInjector(modules = [CustomerListModule::class])
    @ActivityScope
    abstract fun bindCustomerListActivity(): CustomerListActivity


    @ContributesAndroidInjector(modules = [RestaurantTableListModule::class])
    @ActivityScope
    abstract fun bindReservationTableListActivity(): RestaurantTableListActivity
    
}