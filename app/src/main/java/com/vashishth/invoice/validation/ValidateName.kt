package com.vashishth.invoice.validation

class ValidateName{
    fun execute(name : String) : ValidationResult{
        if (name.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "This field can't be blank"
            )
        }else{
            return ValidationResult(
                successful = true
            )
        }
    }
}