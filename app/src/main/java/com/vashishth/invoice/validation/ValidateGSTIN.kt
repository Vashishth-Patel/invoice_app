package com.vashishth.invoice.validation

import java.util.regex.Pattern

class ValidateGSTIN {
    fun execute(GSTIN : String?) : ValidationResult{
        val pattern = ("^[0-9]{2}[A-Z]{5}[0-9]{4}"
                + "[A-Z]{1}[1-9A-Z]{1}"
                + "Z[0-9A-Z]{1}$")
        val p : Pattern = Pattern.compile(pattern)
        if (!GSTIN.isNullOrBlank()){
            if (!p.matcher(GSTIN).matches()){
                return ValidationResult(
                    successful = false,
                    errorMessage = "Enter Valid GSTIN Number"
                )
            }
        }
        return ValidationResult(
            successful = true
        )
    }
}