package com.bendaniel10.reservations.database.dao

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.bendaniel10.reservations.database.entity.Customer

@Dao
interface CustomerDao {

    @Query("SELECT COUNT(id) FROM Customer")
    fun countAll(): Int

    @Query(value = "SELECT * FROM Customer ORDER BY id")
    fun fetchCustomersPaged(): DataSource.Factory<Int, Customer>

    @Insert
    fun insertAll(vararg customers: Customer)


}