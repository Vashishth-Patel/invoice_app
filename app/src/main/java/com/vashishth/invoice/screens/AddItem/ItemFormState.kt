package com.vashishth.invoice.screens.AddItem

data class ItemFormState (
    val itemName : String = "",
    val itemNameError : String? = null,
    val itemNumber: String? = null,
    val itemNumberError : String? = null,
    val price : String = "",
    val priceError : String? = null,
    val unit : String = "",
    val unitError : String? = null,
    val hsnNumber : String? = null,
    val hsnNumberError : String? = null,
    val stock : String = "",
    val stockError : String? = null,
    val gstRate : String? = null,
    val gstRateError : String? = null,
    val stockAddDate : String = "18/12/2002",
    val stockAddDateError : String? = null,
    val purchasePrice : String = "",
    val purchasePriceError : String? = null,
    val description : String? = null,
    val descriptionError : String? = null
        )