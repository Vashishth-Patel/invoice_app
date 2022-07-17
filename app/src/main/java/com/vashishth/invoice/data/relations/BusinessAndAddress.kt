package com.vashishth.invoice.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vashishth.invoice.data.entity.Address
import com.vashishth.invoice.data.entity.Business

data class BusinessAndAddress(
    @Embedded val business: Business,
    @Relation(
        parentColumn = "businessId",
        entityColumn = "id"
    )
    val address: Address
)
