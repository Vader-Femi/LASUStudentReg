package com.femi.lasustudentreg.data.domain.use_case.presentation

data class RegistrationFormState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val matric: String = "",
    val matricError: String? = null,
    val department: String = "",
    val departmentError: String? = null,
    val faculty: String = "",
    val facultyError: String? = null,
    val jambScore: String = "",
    val jambScoreError: String? = null,
)
