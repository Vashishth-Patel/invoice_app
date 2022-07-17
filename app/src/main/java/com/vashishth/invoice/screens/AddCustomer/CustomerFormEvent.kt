package com.vashishth.invoice.screens.AddCustomer

sealed class AllFormEvent {
    //customer form
    data class nameChanged(val name: String) : AllFormEvent()
    data class phoneChanged(val phone: String?) : AllFormEvent()
    data class emailChanged(val email: String?) : AllFormEvent()
    object CustomerSubmit: AllFormEvent()

    //business name form
    data class businessNameChanged(val name: String) : AllFormEvent()
    object BusinessNameSubmit: AllFormEvent()

    //business Detail Form with Address
    data class legalNameChanged(val legalname : String) : AllFormEvent()
    data class PANNumberChanged(val PANNumber : String?) : AllFormEvent()
    data class GSTINNumberChanged(val GSTINNumber : String?) : AllFormEvent()
    data class businessPhoneChanged(val phoneNumber: String) : AllFormEvent()
    data class websiteChanged(val website: String?) : AllFormEvent()
    data class businessEmailChanged(val businessEmail : String?) : AllFormEvent()
    data class businessAddressChanged(val address : String) : AllFormEvent()
    data class PINCodeChanged(val PIN : String?) : AllFormEvent()
    data class StateChanged(val state : String?) : AllFormEvent()
    data class CityChanged(val city : String?) : AllFormEvent()
    object BusinessDetailSubmit : AllFormEvent()

    //item form
    data class itemNameChanged(val itemName : String) : AllFormEvent()
    data class itemNumberChanged(val itemNumber : String?) : AllFormEvent()
    data class itemPriceChanged(val itemPrice : String) : AllFormEvent()
    data class itemUnitChanged(val itemUnit : String) : AllFormEvent()
    data class itemHSNNumberChanged(val itemHSN : String?) : AllFormEvent()
    data class itemStockChanged(val itemStock : String) : AllFormEvent()
    data class itemGSTRateChanged(val itemGST : String?) : AllFormEvent()
    data class itemStockAddDAteChanged(val stockAddDate : String) : AllFormEvent()
    data class itemPurchasePriceChanged(val itemPurchasePrice : String) : AllFormEvent()
    data class itemDescriptionChanged(val itemDescription : String?) : AllFormEvent()
    object AddItem : AllFormEvent()

}