package com.femi.lasustudentreg.data.domain.use_case

import com.femi.e_class.domain.use_case.ValidationResult

class ValidateName {

    fun execute(name: String): ValidationResult {
        if (name.isBlank()){
            return ValidationResult(
                false,
                "Name can't be black"
            )
        }
        return ValidationResult(
            true
        )
    }
}