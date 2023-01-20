package com.femi.lasustudentreg.data.domain.use_case

import androidx.core.text.isDigitsOnly
import com.femi.e_class.domain.use_case.ValidationResult

class ValidateJambScore {

    fun execute(score: String): ValidationResult {
        if (score.isBlank()){
            return ValidationResult(
                false,
                "Jamb score can't be black"
            )
        }
        if (!score.isDigitsOnly()){
            return ValidationResult(
                false,
                "Jamb score must be integer numbers"
            )
        }
        if (score.toInt() !in 1..300){
            return ValidationResult(
                false,
                "Be serious na. That is not your Jamb score"
            )
        }
        return ValidationResult(
            true
        )
    }
}