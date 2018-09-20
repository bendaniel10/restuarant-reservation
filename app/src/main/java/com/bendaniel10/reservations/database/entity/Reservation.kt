package com.bendaniel10.reservations.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
        foreignKeys = [
            ForeignKey(entity = Customer::class, parentColumns = ["id"], childColumns = ["customerId"], onDelete = CASCADE),
            ForeignKey(entity = RestaurantTable::class, parentColumns = ["id"], childColumns = ["restaurantTableId"], onDelete = CASCADE)
        ],

        indices = [
            (Index(value = ["customerId"])),
            (Index(value = ["restaurantTableId"]))
        ]
)
data class Reservation(

        val customerId: Int,

        val restaurantTableId: Int

) {


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}