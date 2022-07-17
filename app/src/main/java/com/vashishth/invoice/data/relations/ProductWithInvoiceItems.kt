package com.vashishth.invoice.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vashishth.invoice.data.entity.InvoiceItem
import com.vashishth.invoice.data.entity.Product

data class ProductWithInvoiceItems(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "itemNumber",
        entityColumn = "itemNumber"
    )
    val invoiceItems : List<InvoiceItem>
)
