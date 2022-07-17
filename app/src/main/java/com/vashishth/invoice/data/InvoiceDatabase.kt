package com.vashishth.invoice.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vashishth.invoice.data.entity.*


@Database(
    entities = [Product::class, Invoice::class, Customer::class,InvoiceItem::class,Business::class,Address::class,BusinessDetail::class,BusinessLogo::class,BusinessSign::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class InvoiceDatabase : RoomDatabase(){

    abstract fun getInvoiceDao() : InvoiceDAO
}