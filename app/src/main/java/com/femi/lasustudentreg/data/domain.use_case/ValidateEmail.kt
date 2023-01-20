package com.femi.lasustudentreg.data.domain.use_case

import android.util.Patterns
import com.femi.e_class.domain.use_case.ValidationResult

class ValidateEmail {

    fun execute(email: String): ValidationResult {
        if (email.isBlank()){
            return ValidationResult(
                false,
                "Email can't be black"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                false,
                "That's not a valid email"
            )
        }
        return ValidationResult(
            true
        )
    }
}