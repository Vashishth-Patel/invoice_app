package com.vashishth.invoice.validation

import android.util.Patterns

class ValidateEmail{
    fun execute(email: String?): ValidationResult {
        if(!email.isNullOrBlank()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "That's not a valid email"
                )
            }
        }
        return ValidationResult(
            successful = true
        )
    }
}