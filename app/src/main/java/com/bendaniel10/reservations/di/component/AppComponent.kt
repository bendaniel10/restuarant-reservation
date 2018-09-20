package com.bendaniel10.reservations.di.component

import com.bendaniel10.reservations.app.ReservationsApplication
import com.bendaniel10.reservations.di.module.AppActivitiesModule
import com.bendaniel10.reservations.di.module.AppModule
import com.bendaniel10.reservations.di.module.DatabaseModule
import com.bendaniel10.reservations.di.scopes.ApplicationScope
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule


@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppActivitiesModule::class,
    AppModule::class,
    DatabaseModule::class
])
@ApplicationScope
interface AppComponent : AndroidInjector<ReservationsApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ReservationsApplication>()

}