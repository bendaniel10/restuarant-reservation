package com.bendaniel10.reservations.backgroundjob

import android.arch.persistence.room.Room
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import androidx.work.Worker
import com.bendaniel10.reservations.app.Constants
import com.bendaniel10.reservations.database.AppDatabase
import java.util.*

class ClearReservationWorker : Worker() {

    companion object {

        const val TAG = "com.bendaniel10.reservations.backgroundjob.ClearReservationWorker"
        const val INTENT_ACTION_RESERVATIONS_CLEARED = "com.bendaniel10.reservations.backgroundjob.ClearReservationWorker.INTENT_ACTION_RESERVATIONS_CLEARED"

    }

    override fun doWork(): Result {

        val database = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, Constants.APP_DATABASE_NAME).build()

        val reservationDao = database.reservationDao()

        val restaurantTableDao = database.restaurantDao()

        reservationDao.clearAllReservations()

        restaurantTableDao.makeAllTablesAvailable()

        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent(INTENT_ACTION_RESERVATIONS_CLEARED))

        Log.d("Worker", "Clearing reservations at " + Date())

        return Result.SUCCESS

    }


}