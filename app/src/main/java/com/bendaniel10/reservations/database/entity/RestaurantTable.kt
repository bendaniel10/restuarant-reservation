package com.bendaniel10.reservations.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class RestaurantTable(

        val available: Boolean

) {


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}