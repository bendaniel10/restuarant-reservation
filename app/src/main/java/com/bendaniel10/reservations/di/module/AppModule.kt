package com.bendaniel10.reservations.di.module

import android.content.Context
import com.bendaniel10.reservations.app.ReservationsApplication
import com.bendaniel10.reservations.di.scopes.ApplicationScope
import dagger.Binds
import dagger.Module


@Module(includes = [NetworkModule::class])
abstract class AppModule {

    @Binds
    @ApplicationScope
    abstract fun provideContext(application: ReservationsApplication): Context


}