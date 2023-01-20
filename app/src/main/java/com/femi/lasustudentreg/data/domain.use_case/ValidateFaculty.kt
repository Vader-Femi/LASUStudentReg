package com.femi.lasustudentreg.data.domain.use_case

import com.femi.e_class.domain.use_case.ValidationResult

class ValidateFaculty {

    fun execute(faculty: String): ValidationResult {

        if (faculty.isBlank()){
            return ValidationResult(
                false,
                "Faculty can't be black"
            )
        }

        if (faculty.length < 3){
            return ValidationResult(
                false,
                "Enter a real faculty"
            )
        }

        return ValidationResult(
            true
        )
    }
}