package com.vashishth.invoice.screens.AddCustomer

sealed class CustomerFormEvent {
    //customer form
    data class nameChanged(val name: String) : CustomerFormEvent()
    data class phoneChanged(val phone: String?) : CustomerFormEvent()
    data class emailChanged(val email: String?) : CustomerFormEvent()
    object CustomerSubmit: CustomerFormEvent()
}
sealed class businessFormEvent{
    //business name form
    data class businessNameChanged(val name: String) : businessFormEvent()
    object BusinessNameSubmit: businessFormEvent()

    //business Detail Form with Address
    data class legalNameChanged(val legalname : String) : businessFormEvent()
    data class PANNumberChanged(val PANNumber : String?) : businessFormEvent()
    data class GSTINNumberChanged(val GSTINNumber : String?) : businessFormEvent()
    data class businessPhoneChanged(val phoneNumber: String) : businessFormEvent()
    data class websiteChanged(val website: String?) : businessFormEvent()
    data class businessEmailChanged(val businessEmail : String?) : businessFormEvent()
    data class businessAddressChanged(val address : String) : businessFormEvent()
    data class PINCodeChanged(val PIN : String?) : businessFormEvent()
    data class StateChanged(val state : String?) : businessFormEvent()
    data class CityChanged(val city : String?) : businessFormEvent()
    object BusinessDetailSubmit : businessFormEvent()
}

sealed class itemFormEvent{
    data class itemNameChanged(val itemName : String) : itemFormEvent()
    data class itemNumberChanged(val itemNumber : String?) : itemFormEvent()
    data class itemPriceChanged(val itemPrice : String) : itemFormEvent()
    data class itemUnitChanged(val itemUnit : String) : itemFormEvent()
    data class itemHSNNumberChanged(val itemHSN : String?) : itemFormEvent()
    data class itemStockChanged(val itemStock : String) : itemFormEvent()
    data class itemGSTRateChanged(val itemGST : String?) : itemFormEvent()
    data class itemStockAddDAteChanged(val stockAddDate : String) : itemFormEvent()
    data class itemPurchasePriceChanged(val itemPurchasePrice : String) : itemFormEvent()
    data class itemDescriptionChanged(val itemDescription : String?) : itemFormEvent()
    object AddItem : itemFormEvent()
}

sealed class invoiceItemFormEvent{
    data class lineNumberChanged(val lineNumber: String) : invoiceItemFormEvent()
    data class invoiceNumberChanged(val invoiceNumber: String) : invoiceItemFormEvent()
    data class itemNameChanged(val itemName: String) : invoiceItemFormEvent()
    data class itemCountChanged(val itemCount : String) : invoiceItemFormEvent()
    data class unitPriceChanged(val unitPrice : String) : invoiceItemFormEvent()
    object addInvoiceItem : invoiceItemFormEvent()

}

sealed class cartItemFormEvent{
    data class cartItemNameAdd(val itemName : String) : cartItemFormEvent()
    data class cartItemCount(val itemCount: String) : cartItemFormEvent()
    object AddItemToCart : cartItemFormEvent()
}

sealed class invoiceFromEvent{
    data class invoiceNumberChanged(val invoiceNumber : String) : invoiceFromEvent()
    data class entryDateChanged(val entryDate : String) : invoiceFromEvent()
    data class customerRegNoChanged(val customerRegNo : String) : invoiceFromEvent()
    object GenerateInvoice : invoiceFromEvent()
}