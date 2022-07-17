package com.vashishth.invoice.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)