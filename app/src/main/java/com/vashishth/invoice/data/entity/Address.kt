package com.vashishth.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Address(
    val address: String,
    val PINCode : Int?,
    val state : String?,
    val city : String?
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 1
}
