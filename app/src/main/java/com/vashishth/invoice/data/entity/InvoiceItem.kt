package com.vashishth.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InvoiceItem(
    @PrimaryKey(autoGenerate = true)
    val invoiceNumber : Int,
    val itemName : String,
    val quantity : Int
)
