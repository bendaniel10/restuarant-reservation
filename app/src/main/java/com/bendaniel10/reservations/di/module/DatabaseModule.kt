package com.bendaniel10.reservations.di.module

import com.bendaniel10.reservations.app.ReservationsApplication
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {


    @Provides
    @ApplicationScope
    fun provideDatabase(application: ReservationsApplication): AppDatabase = application.database


}