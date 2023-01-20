package com.femi.lasustudentreg.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.femi.lasustudentreg.data.domain.use_case.*
import com.femi.lasustudentreg.data.presentation.RegistrationFormEvent
import com.femi.lasustudentreg.data.presentation.RegistrationFormState
import com.femi.lasustudentreg.data.repository.MainActivityRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val repository: MainActivityRepository,
    private val validateDepartment: ValidateDepartment = ValidateDepartment(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validateFaculty: ValidateFaculty = ValidateFaculty(),
    private val validateJambScore: ValidateJambScore = ValidateJambScore(),
    private val validateMatric: ValidateMatric = ValidateMatric(),
    private val validateFirstName: ValidateName = ValidateName(),
    private val validateLastName: ValidateName = ValidateName(),
): ViewModel() {

    var registrationFormState by mutableStateOf(RegistrationFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val registrationEventChannel = Channel<RegistrationEvent<Any?>>()
    val registrationEvents = registrationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.DepartmentChanged -> {
                registrationFormState =
                    registrationFormState.copy(department = event.department)
            }
            is RegistrationFormEvent.EmailChanged -> {
                registrationFormState =
                    registrationFormState.copy(email = event.email)
            }
            is RegistrationFormEvent.FacultyChanged -> {
                registrationFormState =
                    registrationFormState.copy(faculty = event.faculty)
            }
            is RegistrationFormEvent.FirstNameChanged -> {
                registrationFormState =
                    registrationFormState.copy(firstName = event.firstName)
            }
            is RegistrationFormEvent.JambScoreChanged -> {
                registrationFormState =
                    registrationFormState.copy(jambScore = event.jambScore)
            }
            is RegistrationFormEvent.LastNameChanged -> {
                registrationFormState =
                    registrationFormState.copy(lastName = event.lastName)
            }
            is RegistrationFormEvent.MatricChanged -> {
                registrationFormState =
                    registrationFormState.copy(matric = event.matric)
            }
            RegistrationFormEvent.Submit -> {
                validateRegistrationData()
            }
        }
    }

    private fun validateRegistrationData() {
        val departmentResult = validateDepartment.execute(registrationFormState.department)
        val emailResult = validateEmail.execute(registrationFormState.email)
        val facultyResult = validateFaculty.execute(registrationFormState.faculty)
        val jambScoreResult = validateJambScore.execute(registrationFormState.jambScore)
//        val matricResult = validateMatric.execute(registrationFormState.matric)
        val firstNameResult = validateFirstName.execute(registrationFormState.firstName)
        val lastNameResult = validateLastName.execute(registrationFormState.lastName)


        val hasError = listOf(
            departmentResult,
            emailResult,
            facultyResult,
            jambScoreResult,
//            matricResult,
            firstNameResult,
            lastNameResult,
        ).any { !it.successful }

        registrationFormState = registrationFormState.copy(
            departmentError = departmentResult.errorMessage,
            emailError = emailResult.errorMessage,
            facultyError = facultyResult.errorMessage,
            jambScoreError = jambScoreResult.errorMessage,
//            matricError = matricResult.errorMessage,
            firstNameError = firstNameResult.errorMessage,
            lastNameError = lastNameResult.errorMessage,
        )

        if (hasError)
            return

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    fun createUser(){
        val email = registrationFormState.email
        val lastName = registrationFormState.lastName

        viewModelScope.launch {
            registrationEventChannel.send(RegistrationEvent.Loading)
        }

        repository.getFirebaseAuth()
            .createUserWithEmailAndPassword(email, "LASU_${lastName.uppercase()}")
            .addOnCompleteListener { addUserTask ->
                if (addUserTask.isSuccessful) {
                    viewModelScope.launch {
                        submitRegistrationData()
                    }
                } else {
                    viewModelScope.launch {
                        registrationEventChannel.send(RegistrationEvent.Error(addUserTask.exception))
                    }
                }
            }

    }

    fun submitRegistrationData() {
        val department = registrationFormState.department
        val email = registrationFormState.email
        val faculty = registrationFormState.faculty
        val jambScore = registrationFormState.jambScore
//        val matric = registrationFormState.matric
        val firstName = registrationFormState.firstName
        val lastName = registrationFormState.lastName

        val userHashMap = hashMapOf(
            "Department" to department,
            "Email" to email,
            "Faculty" to faculty,
            "JambScore" to jambScore,
//            "Matric" to matric,
            "FirstName" to firstName,
            "LastName" to lastName,
        )

        viewModelScope.launch {
            repository.getCollectionReference()
                .document(email)
                .set(userHashMap)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        registrationEventChannel.send(RegistrationEvent.Success)
                    }
                }.addOnFailureListener {
                    viewModelScope.launch {
                        registrationEventChannel.send(RegistrationEvent.Error(it))
                    }
                }
        }

    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

    sealed class RegistrationEvent<out T> {
        object Success : RegistrationEvent<Nothing>()
        data class Error(val exception: java.lang.Exception?) : RegistrationEvent<Nothing>()
        object Loading : RegistrationEvent<Nothing>()
    }

}