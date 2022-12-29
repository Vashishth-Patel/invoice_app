package com.vashishth.invoice.screens.AddInvoice

import java.text.SimpleDateFormat
import java.time.LocalDate

data class InvoiceFormState(
    val customerRegNumber : String = "",
    val customerRegNumberError : String? = null,
    val entryDate : String = LocalDate.now().toString(),
    val entryDateError : String? = null,
)

data class InvoiceItemState(
    val invoiceNumber : String = "",
    val itemName : String = "",
    val quantity : String = "",
    val unitPrice : String = ""
)
