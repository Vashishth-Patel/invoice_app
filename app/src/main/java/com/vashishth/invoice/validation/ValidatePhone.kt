package com.vashishth.invoice.validation

class ValidatePhone{
    fun execute(phone : String?) : ValidationResult{
        if (phone != null){
            if (phone.length != 10){
                return ValidationResult(
                    successful = false,
                    errorMessage = "Phone number should be of 10 digits"
                )
            }
        }
        return ValidationResult(
            successful = true
        )
    }
}