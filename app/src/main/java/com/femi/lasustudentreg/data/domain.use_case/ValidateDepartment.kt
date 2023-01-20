package com.femi.lasustudentreg.data.domain.use_case

import com.femi.e_class.domain.use_case.ValidationResult

class ValidateDepartment {

    fun execute(dept: String): ValidationResult {

        if (dept.isBlank()){
            return ValidationResult(
                false,
                "Department can't be black"
            )
        }

        if (dept.length < 3){
            return ValidationResult(
                false,
                "Enter a real department"
            )
        }

        return ValidationResult(
            true
        )
    }
}