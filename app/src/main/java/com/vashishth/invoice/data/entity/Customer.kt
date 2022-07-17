package com.vashishth.invoice.data.entity

import android.provider.ContactsContract
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Customer(
    val name: String,
    val phoneNumber: Long?,
    val email: String?
){
    @PrimaryKey(autoGenerate = true)
    var  customerRegNo: Int = 0
}
