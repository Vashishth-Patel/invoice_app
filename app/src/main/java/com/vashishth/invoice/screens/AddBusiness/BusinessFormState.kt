package com.vashishth.invoice.screens.AddBusiness

data class BusinessFormState(
    val name: String = "",
    val nameError: String? = null,
)

data class BusinessDetailFormState(
    val legalName :  String = "",
    val legalNameError :  String? = null,
    val PANNumber : String? = null,
    val PANNumberError : String? = null,
    val Gstin : String? = null,
    val GstinError : String? = null,
    val phoneNumber : String = "",
    val phoneNumberError : String? = null,
    val website : String? = null,
    val websiteError : String? = null,
    val email : String? = null,
    val emailError : String? = null,
    val address: String = "",
    val addressError: String? = null,
    val PINCode : String? = null,
    val PINCodeError : String? = null,
    val state : String? = null,
    val stateError : String? = null,
    val city : String? = null,
    val cityError : String? = null
)