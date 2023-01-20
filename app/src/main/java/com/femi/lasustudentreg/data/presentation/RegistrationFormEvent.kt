package com.femi.lasustudentreg.data.presentation

sealed class RegistrationFormEvent{
    data class DepartmentChanged(val department: String): RegistrationFormEvent()
    data class EmailChanged(val email: String): RegistrationFormEvent()
    data class FacultyChanged(val faculty: String): RegistrationFormEvent()
    data class JambScoreChanged(val jambScore: String): RegistrationFormEvent()
    data class MatricChanged(val matric: String): RegistrationFormEvent()
    data class FirstNameChanged(val firstName: String): RegistrationFormEvent()
    data class LastNameChanged(val lastName: String): RegistrationFormEvent()

    object Submit: RegistrationFormEvent()
}
