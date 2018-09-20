package com.bendaniel10.reservations.app

import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatDelegate
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bendaniel10.reservations.BuildConfig
import com.bendaniel10.reservations.backgroundjob.ClearReservationWorker
import com.bendaniel10.reservations.database.AppDatabase
import com.bendaniel10.reservations.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import java.util.concurrent.TimeUnit


class ReservationsApplication : DaggerApplication() {

    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else ReleaseTree())

        this.database = Room.databaseBuilder(this,
                AppDatabase::class.java, Constants.APP_DATABASE_NAME).build()

        setUpReservationCleanerBackgroundJob()

    }

    private fun setUpReservationCleanerBackgroundJob() {

        val constraints = Constraints.Builder()
                .build()

        val clearReservationWork = PeriodicWorkRequest.Builder(ClearReservationWorker::class.java, 15, TimeUnit.MINUTES)
                .addTag(ClearReservationWorker.TAG)
                .setConstraints(constraints).build()

        WorkManager.getInstance().enqueue(clearReservationWork)

    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder().create(this)


    private class ReleaseTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            //No Logs on Release builds.
        }
    }

}