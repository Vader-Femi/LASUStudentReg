package com.femi.lasustudentreg.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.femi.lasustudentreg.R
import com.femi.lasustudentreg.ui.navigation.Navigation
import com.femi.lasustudentreg.data.presentation.RegistrationFormEvent
import com.femi.lasustudentreg.data.repository.MainActivityRepository
import com.femi.lasustudentreg.handleNetworkExceptions
import com.femi.lasustudentreg.ui.theme.LASUStudentRegTheme
import com.femi.lasustudentreg.ui.viewmodel.MainActivityViewModel
import com.femi.lasustudentreg.ui.viewmodel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainActivityViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setContent {
            LASUStudentRegTheme(dynamicColor = false) {
                Surface {
                    val navController = rememberNavController()
                    Scaffold(
                        topBar = {
                            AppBar(title = getString(R.string.app_name))
                        },
                        content = { paddingValue ->
                            Box(modifier = Modifier.padding(paddingValue)) {
                                Navigation(
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }
                        }
                    )
                }
            }
        }
    }


    private fun setupViewModel() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val fireStoreReference = FirebaseFirestore.getInstance().collection("Students")
        val repository = MainActivityRepository(firebaseAuth, fireStoreReference)
        val viewModelFactory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(viewModel: MainActivityViewModel) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val state = viewModel.registrationFormState
    var facultyExpanded by remember { mutableStateOf(false) }
    val faculties = listOf("Science",
        "Art",
        "Education",
        "Law",
        "Management Science",
        "Social Science",
        "School of Communication")
    var accountCreated by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is MainActivityViewModel.ValidationEvent.Success -> {
                    viewModel.createUser()
                }
            }
        }
    }

    LaunchedEffect(key1 = viewModel.registrationEvents) {
        viewModel.registrationEvents.collect { event ->
            loading = (event is MainActivityViewModel.RegistrationEvent.Loading)
            when (event) {
                is MainActivityViewModel.RegistrationEvent.Error -> {
                    context.handleNetworkExceptions(event.exception)
                }
                MainActivityViewModel.RegistrationEvent.Loading -> {

                }
                MainActivityViewModel.RegistrationEvent.Success -> {
                    accountCreated = true
                }
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(20.dp, 30.dp, 20.dp, 50.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start),
            text = "Create your LASU profile",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            modifier = Modifier
                .align(Alignment.Start),
            text = "Enter your correct details to get started",
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(
            value = state.firstName,
            label = { Text(text = "First Name") },
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.FirstNameChanged(it))
            },
            isError = state.firstNameError != null,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            leadingIcon = {
                Icon(Icons.Filled.Person, "First Name Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        if (state.firstNameError != null) {
            Text(
                text = state.firstNameError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.lastName,
            label = { Text(text = "Last Name") },
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.LastNameChanged(it))
            },
            isError = state.lastNameError != null,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            leadingIcon = {
                Icon(Icons.Filled.Person, "Last Name Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        if (state.lastNameError != null) {
            Text(
                text = state.lastNameError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.email,
            label = { Text(text = "Email") },
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.EmailChanged(it))
            },
            isError = state.emailError != null,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            leadingIcon = {
                Icon(Icons.Filled.Email, "Email Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        if (state.emailError != null) {
            Text(
                text = state.emailError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        ExposedDropdownMenuBox(
            expanded = facultyExpanded,
            onExpandedChange = {
                facultyExpanded = !facultyExpanded
            },
        ) {
            OutlinedTextField(
                readOnly = true,
                value = state.faculty,
                label = { Text(text = "Faculty") },
                onValueChange = {
                    viewModel.onEvent(RegistrationFormEvent.FacultyChanged(it))
                },
                isError = state.facultyError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                maxLines = 2,
                leadingIcon = {
                    Icon(Icons.Filled.LocationOn, "Faculty Icon")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = facultyExpanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = facultyExpanded,
                onDismissRequest = { facultyExpanded = false },
            ) {
                faculties.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            viewModel.onEvent(RegistrationFormEvent.FacultyChanged(selectionOption))
                            facultyExpanded = false
                        },
                        text = {
                            Text(text = selectionOption)
                        }
                    )
                }
            }
        }
        if (state.facultyError != null) {
            Text(
                text = state.facultyError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.department,
            label = { Text(text = "Department") },
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.DepartmentChanged(it))
            },
            isError = state.departmentError != null,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            leadingIcon = {
                Icon(Icons.Filled.LocationOn, "Department Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Next
            )
        )
        if (state.departmentError != null) {
            Text(
                text = state.departmentError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = state.jambScore,
            label = { Text(text = "Jamb Score") },
            onValueChange = {
                viewModel.onEvent(RegistrationFormEvent.JambScoreChanged(it))
            },
            isError = state.jambScoreError != null,
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 2,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_class),
                    contentDescription = "Jamb Score Icon")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Done
            )
        )
        if (state.jambScoreError != null) {
            Text(
                text = state.jambScoreError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        if (loading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(40.dp))
        }
        Button(
            onClick = {
                viewModel.onEvent(RegistrationFormEvent.Submit)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            enabled = !loading
        ) {
            Text(text = "Register")
        }
        if (accountCreated) {
            AccountCreatedDialog(
                modifier = Modifier
                    .padding(0.dp, 30.dp, 0.dp, 0.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun AccountCreatedDialog(
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        confirmButton = {},
        modifier = modifier
            .fillMaxWidth(0.9f),
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f),
                    painter = painterResource(id = R.drawable.leader_pana),
                    contentDescription = "Registration Complete Icon"
                )
                Text(
                    text = "Successful!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 15.dp, 0.dp, 10.dp)
                )
                Text(
                    text = "Your registration data has been received",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp, 0.dp, 20.dp),
                )
                Button(
                    onClick = {
                        exitProcess(0)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                ) {
                    Text(text = "Exit")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 20.dp,
        onDismissRequest = {
//            exitProcess(0)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp)
        )
    )
}

//@Preview(showBackground = true) )
@Composable
fun WelcomeScreen(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 10.dp, 0.dp, 30.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(30.dp),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                ),
                text = "Welcome to LASU student registration",
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(0.9f),
                onClick = {
                    navController.navigate("user_details_route")
                }
            ) {
                Text(text = "Next")
            }
        }
    }

}
