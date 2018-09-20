package com.bendaniel10.reservations.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.bendaniel10.reservations.database.entity.Reservation

@Dao
interface ReservationDao {

    @Insert
    fun insertAll(vararg reservation: Reservation)

    @Query("SELECT * FROM Reservation WHERE customerId=:customerId LIMIT 1")
    fun fetchReservationByCustomer(customerId: Int): Reservation

    @Query("SELECT COUNT(*) FROM Reservation WHERE customerId=:customerId LIMIT 1")
    fun countReservationByCustomer(customerId: Int): Int

    @Query("SELECT COUNT(*) FROM Reservation WHERE restaurantTableId=:restaurantTableId LIMIT 1")
    fun countReservationByRestaurantTable(restaurantTableId: Int): Int

    @Query("DELETE FROM Reservation")
    fun clearAllReservations()

}