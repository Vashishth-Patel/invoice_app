package com.vashishth.invoice.validation

import androidx.core.text.isDigitsOnly

class ValidateNumber{
    fun execute(number : String?) : ValidationResult{
        if (number != null){
            if (!number.isDigitsOnly()){
                return ValidationResult(
                    successful = false,
                    errorMessage = "This field contains Digits Only"
                )
            }
        }
        return ValidationResult(
            successful = true
        )
    }
}