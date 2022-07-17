package com.vashishth.invoice.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vashishth.invoice.data.entity.Customer
import com.vashishth.invoice.data.entity.Invoice

data class CustomerWithInvoices(
    @Embedded val customer: Customer,
    @Relation(
        parentColumn = "customerRegNo",
        entityColumn = "customerRegNo"
    )
    val invoices : List<Invoice>
)
