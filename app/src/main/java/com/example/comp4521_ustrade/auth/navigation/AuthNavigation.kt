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
import com.example.comp4521_ustrade.auth.screens.LoginScreen
import com.example.comp4521_ustrade.auth.screens.RegisterScreen
import com.example.comp4521_ustrade.auth.screens.ForgotPasswordScreen
import com.example.comp4521_ustrade.auth.screens.LandingScreen

sealed class AuthScreen(val route: String) {
    object Landing : AuthScreen("landing")
    object Login : AuthScreen("login")
    object Register : AuthScreen("register")
    object ForgotPassword : AuthScreen("forgot_password")
}

@Composable
fun AuthNavigation(
    navController: NavHostController = rememberNavController(),
    onAuthSuccess: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val user by authViewModel.user.collectAsState()
    
    // Check if user is logged in
    user?.let {
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
            LoginScreen(
                onSignIn = { email, password ->
                    authViewModel.signIn(email, password)
                },
                onNavigateToRegister = {
                    navController.navigate(AuthScreen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(AuthScreen.ForgotPassword.route)
                }
            )
        }
        
        composable(AuthScreen.Register.route) {
            RegisterScreen(
                onSignUp = { email, password ->
                    authViewModel.signUp(email, password)
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
                    navController.navigateUp()
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
    }
} 