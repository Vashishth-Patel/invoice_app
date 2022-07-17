package com.vashishth.invoice.screens.AddCustomer

data class CustomerFormState(
    val name: String = "",
    val nameError: String? = null,
    val phone: String? = null,
    val phoneError: String? = null,
    val email: String? = null,
    val emailError: String? = null,
)