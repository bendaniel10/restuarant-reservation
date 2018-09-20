package com.bendaniel10.reservations.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class Customer(

    @PrimaryKey()
    val id: Int,

    val customerFirstName: String,

    val customerLastName: String

)  : Serializable