package com.vashishth.invoice.data.entity

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Business(

    val legalName :  String,
    val PANNumber : String?,
    val Gstin : String?,
    val phoneNumber : Long,
    val website : String?,
    val email : String?,

){
    @PrimaryKey(autoGenerate = true)
    var businessId : Int =1
}

@Entity
data class BusinessLogo(
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val businessLogo : ByteArray

){
    @PrimaryKey(autoGenerate = true)
    var imgId : Int = 1
}

@Entity
data class BusinessSign(
    val signature : ByteArray
){
    @PrimaryKey(autoGenerate = true)
    var imgSignId : Int = 1
}