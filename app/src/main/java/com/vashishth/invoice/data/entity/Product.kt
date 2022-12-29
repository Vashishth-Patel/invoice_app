package com.vashishth.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Product(
    val itemName : String,
    val price : Double,
    val unit : String,
    val hsnNumber : Int?,
    val stock : Int,
    val GSTRate : String?,
    val stockAddDate : Date,
    val purchasePrice : Double,
    val description : String?
){
    @PrimaryKey(autoGenerate = true)
    var itemNumber : Int = 0
}

data class itemView(
    val itemName: String,
    val price: Double,
    val unit: String,
    val stock: Int
)
