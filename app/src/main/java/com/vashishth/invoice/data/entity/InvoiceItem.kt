package com.vashishth.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InvoiceItem(
    @PrimaryKey(autoGenerate = true)
    val lineNo : Int = 0,
    val invoiceNumber : Int,
    val itemName : String,
    var quantity : Int,
    val unitPrice : Double
)
