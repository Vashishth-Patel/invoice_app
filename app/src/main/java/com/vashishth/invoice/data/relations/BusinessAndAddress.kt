package com.vashishth.invoice.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vashishth.invoice.data.entity.Address
import com.vashishth.invoice.data.entity.Business
import com.vashishth.invoice.data.entity.Cart
import com.vashishth.invoice.data.entity.Product

data class BusinessAndAddress(
    @Embedded val business: Business,
    @Relation(
        parentColumn = "businessId",
        entityColumn = "id"
    )
    val address: Address
)

//data class CartAndProduct(
//    @Embedded val product: Product,
//    @Relation(
//        parentColumn = "itemName",
//        entityColumn = "itemName"
//    )
//    val cart: Cart
//)
