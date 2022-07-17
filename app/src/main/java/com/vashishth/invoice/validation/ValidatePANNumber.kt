package com.vashishth.invoice.validation

import java.util.regex.Pattern

class ValidatePANNumber{
    val pattern = "[A-Z]{5}[0-9]{4}[A-Z]{1}"
    val p : Pattern = Pattern.compile(pattern)
    fun execute(PANNUmber : String?) : ValidationResult{
        if (!PANNUmber.isNullOrBlank()){
            if (!p.matcher(PANNUmber).matches()){
                return ValidationResult(
                    successful = false,
                    errorMessage = "Enter Valid PAN Number"
                )
            }
        }
        return ValidationResult(
            successful = true
        )
    }
}