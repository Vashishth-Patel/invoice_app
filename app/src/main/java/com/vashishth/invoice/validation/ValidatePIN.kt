package com.vashishth.invoice.validation

import android.util.Patterns
import java.util.regex.Pattern

class ValidatePIN{
    fun execute(pin : String?) : ValidationResult{
        val pattern = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$"
        val p : Pattern = Pattern.compile(pattern)
        if(pin != null) {
            if (!p.matcher(pin).matches()) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "That's not a valid PIN"
                )
            }
        }
        return ValidationResult(
            successful = true
        )
    }
}