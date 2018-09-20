package com.bendaniel10.reservations.database.dao

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.bendaniel10.reservations.database.entity.RestaurantTable

@Dao
interface RestaurantTableDao {

    @Query("SELECT COUNT(id) FROM RestaurantTable")
    fun countAll(): Int

    @Insert
    fun insertAll(vararg restaurantTable: RestaurantTable): Array<Long>


    @Query(value = "SELECT * FROM RestaurantTable ORDER BY id")
    fun fetchRestaurantTablePaged(): DataSource.Factory<Int, RestaurantTable>


    @Query("UPDATE RestaurantTable SET available = 1")
    fun makeAllTablesAvailable()
}