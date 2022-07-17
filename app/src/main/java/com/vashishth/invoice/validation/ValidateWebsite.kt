package com.vashishth.invoice.validation

import android.util.Patterns

class ValidateWebsite {
    fun execute(website : String?) : ValidationResult{
        if(!website.isNullOrBlank()) {
            if (!Patterns.WEB_URL.matcher(website).matches()) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "That's not a valid Website"
                )
            }
        }
        return ValidationResult(
            successful = true
        )
    }
}