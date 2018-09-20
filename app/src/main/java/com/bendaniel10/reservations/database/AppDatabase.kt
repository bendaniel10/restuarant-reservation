package com.bendaniel10.reservations.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.bendaniel10.reservations.database.dao.CustomerDao
import com.bendaniel10.reservations.database.dao.ReservationDao
import com.bendaniel10.reservations.database.dao.RestaurantTableDao
import com.bendaniel10.reservations.database.entity.Customer
import com.bendaniel10.reservations.database.entity.Reservation
import com.bendaniel10.reservations.database.entity.RestaurantTable

@Database(entities = [
    Customer::class,
    RestaurantTable::class,
    Reservation::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao

    abstract fun restaurantDao(): RestaurantTableDao

    abstract fun reservationDao(): ReservationDao

}