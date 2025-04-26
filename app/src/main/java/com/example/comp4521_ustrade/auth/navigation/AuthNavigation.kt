package com.example.comp4521_ustrade.auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.comp4521_ustrade.auth.AuthViewModel
import com.example.comp4521_ustrade.auth.screens.ForgotPasswordScreen
import com.example.comp4521_ustrade.auth.screens.LandingScreen
import com.example.comp4521_ustrade.auth.screens.LoginScreen
import com.example.comp4521_ustrade.auth.screens.RegisterScreen

sealed class AuthScreen(val route: String) {
    object Landing : AuthScreen("landing")
    object Login : AuthScreen("login")
    object Register : AuthScreen("register")
    object ForgotPassword : AuthScreen("forgot_password")
}

@Composable
fun AuthNavigation(
    navController: NavHostController = rememberNavController(),
    onAuthSuccess: @Composable () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val user by authViewModel.user.collectAsState()
    
    // Check if user is logged in
    user?.let {
//        Text("Debug: User is logged in")
        onAuthSuccess()
        return
    }

    NavHost(
        navController = navController,
        startDestination = AuthScreen.Landing.route
    ) {
        composable(AuthScreen.Landing.route) {
            LandingScreen(
                onNavigateToLogin = {
                    navController.navigate(AuthScreen.Login.route)
                },
                onNavigateToRegister = {
                    navController.navigate(AuthScreen.Register.route)
                }
            )
        }
        
        composable(AuthScreen.Login.route) {
            val authState by authViewModel.authState.collectAsState()
            LoginScreen(
                onSignIn = { email, password ->
                    authViewModel.signIn(email, password)
                },
                onNavigateToRegister = {
                    navController.navigate(AuthScreen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(AuthScreen.ForgotPassword.route)
                },
                authState = authState
            )
        }
        
        composable(AuthScreen.Register.route) {
            RegisterScreen(
                onSignUp = { firstName, lastName,email, password ->
                    authViewModel.signUp(email, password, firstName, lastName)
                    print("$firstName $lastName")
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
        
        composable(AuthScreen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onResetPassword = { email ->
                    authViewModel.resetPassword(email)
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
    }
} 