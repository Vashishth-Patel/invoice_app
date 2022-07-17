package com.vashishth.invoice.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.vashishth.invoice.data.entity.Invoice
import com.vashishth.invoice.data.entity.InvoiceItem

data class InvoiceWithInvoiceItems(
    @Embedded val invoice: Invoice,
    @Relation(
        parentColumn = "invoiceNumber",
        entityColumn = "invoiceNumber"
    )
    val invoiceItems : List<InvoiceItem>
)
