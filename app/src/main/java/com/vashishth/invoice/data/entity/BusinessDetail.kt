package com.vashishth.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BusinessDetail(
    val businessName : String
){
    @PrimaryKey(autoGenerate = true)
    var businessId : Int = 1
}
