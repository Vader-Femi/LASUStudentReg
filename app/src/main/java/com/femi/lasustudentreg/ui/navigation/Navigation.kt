package com.femi.lasustudentreg.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.femi.lasustudentreg.ui.UserDetailsScreen
import com.femi.lasustudentreg.ui.WelcomeScreen
import com.femi.lasustudentreg.viewmodel.MainActivityViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MainActivityViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home_route",
        route = "root_route") {

        composable("home_route"){
            WelcomeScreen(navController)
        }

        composable("user_details_route"){
            UserDetailsScreen(viewModel = viewModel)
        }

    }
}