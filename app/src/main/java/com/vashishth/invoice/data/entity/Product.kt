package com.vashishth.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Product(
    @PrimaryKey(autoGenerate = false)
    val itemName : String,
    val itemNumber : Int?,
    val price : Double,
    val unit : String,
    val hsnNumber : Int?,
    val stock : Int,
    val GSTRate : String?,
    val stockAddDate : Date,
    val purchasePrice : Double,
    val description : String?
)
