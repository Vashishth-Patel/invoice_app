package com.vashishth.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart(
    @PrimaryKey
    val itemName : String,
    val quantity : Int
    )
