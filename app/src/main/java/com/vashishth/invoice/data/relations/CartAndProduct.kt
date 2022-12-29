package com.vashishth.invoice.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vashishth.invoice.data.entity.Cart
import com.vashishth.invoice.data.entity.Product
data class CartAndProduct (
    @Embedded val product : Product,
    @Relation(
        parentColumn = "itemName",
        entityColumn = "itemName"
    )
    val cart: Cart
    )
