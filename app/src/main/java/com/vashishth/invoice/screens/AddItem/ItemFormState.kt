package com.vashishth.invoice.screens.AddItem

data class ItemFormState (
    val itemName : String = "",
    val itemNameError : String? = null,
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
    val stockAddDate : String = "01-01-2023",
    val stockAddDateError : String? = null,
    val purchasePrice : String = "",
    val purchasePriceError : String? = null,
    val description : String? = null,
    val descriptionError : String? = null
        )



data class CartItemState(
    val itemName : String = "",
    val itemNameError: String? = null,
    val itemCount : String = "0",
    val itemCountError : String? = null
)