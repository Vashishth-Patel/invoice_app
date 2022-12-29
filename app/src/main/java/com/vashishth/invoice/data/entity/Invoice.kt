package com.vashishth.invoice.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date

@Entity
data class Invoice(
    val customerRegNo : Int,
    val entryDate: Date = Date.from(Instant.now())
){
    @PrimaryKey(autoGenerate = true)
    var invoiceNumber: Int = 0
}
