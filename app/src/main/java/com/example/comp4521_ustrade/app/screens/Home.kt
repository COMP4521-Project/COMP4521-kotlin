package com.example.comp4521_ustrade.app.screens

import AboutAppScreen
import EditPasswordScreen
import EditProfileScreen
import NotificationSettingsScreen
import PreferencesScreen
import ProfilePreviewScreen
import Settings
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.comp4521_ustrade.app.components.CourseMenu
import com.example.comp4521_ustrade.app.components.DrawerContent
import com.example.comp4521_ustrade.app.components.USTBottomBar
import com.example.comp4521_ustrade.app.components.USTPager
import com.example.comp4521_ustrade.app.components.USTTopBar
import com.example.comp4521_ustrade.app.display.DisplayCourseCards
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.auth.AuthViewModel
import com.example.comp4521_ustrade.auth.screens.LandingScreen
import com.example.comp4521_ustrade.ui.theme.USTBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit){

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val userViewModel : UserViewModel = viewModel()
    val navViewModel : NavViewModel = viewModel()

    val onOpenDrawer: () -> Unit = {
        scope.launch {
            if (drawerState.isClosed) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(200.dp)
            ) {
                DrawerContent(
                    navController = navigationController,
                    drawerState = drawerState
                )
            }
        }
    ) {
        NavHost(
            navController = navigationController,
            startDestination = Screens.Home.screen,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 300)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) }
        ) {
            composable(Screens.Home.screen) {
                Scaffold(
                    topBar = { USTTopBar(onOpenDrawer = onOpenDrawer, navigationController) },
                    bottomBar = { USTBottomBar(navigationController,  navViewModel = navViewModel) },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(innerPadding)
                    ) {

                        USTPager()
                        CourseMenu({ DisplayCourseCards(navigateController=navigationController) })

                    }
                }
            }
            composable(Screens.Download.screen) {
                DocumentListScreen(
                pageTitle = "downloaded",
                onNavigateBack = { navigationController.navigateUp() },
                onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) }
            )}
            composable(Screens.Profile.screen) {
                Scaffold(
                    bottomBar = { USTBottomBar(navigationController, navViewModel = navViewModel) },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(USTBlue)
                            .padding(innerPadding)
                    ) {
                        Profile(
                            navigationController = navigationController,
                            authViewModel = authViewModel,
                            userViewModel = userViewModel
                        )

                    }
                }
            }


            composable(Screens.ProfilePreview.screen) {
                ProfilePreviewScreen(
                    onNavigateBack = { navigationController.navigateUp() },
                    navigationController = navigationController,
                    userViewModel = userViewModel
                )
            }
            composable(Screens.Notification.screen) { Notification(
                onNavigateBack = { navigationController.navigateUp() },
            ) }
            composable(Screens.Search.screen) {
                Scaffold(
                    topBar = { Search() },
                    bottomBar = { USTBottomBar(navigationController, navViewModel = navViewModel) },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(innerPadding)
                    ) {
                        // Add content here if needed
                    }
                }
            }
            composable(Screens.Favorite.screen) { Favorite() }
            composable(Screens.ChatRoom.screen) { ChatRoom() }
            composable(Screens.Settings.screen) {
                Settings(
                    onNavigateBack = { navigationController.navigateUp() },
                    navigationController = navigationController,
                    authViewModel = authViewModel
                )
            }
            // Setting page
            composable(Screens.EditProfile.screen) {
                EditProfileScreen(
                    onNavigateBack = { navigationController.navigateUp() }
                )
            }
            composable(Screens.EditPassword.screen) {
                EditPasswordScreen(
                    onNavigateBack = { navigationController.navigateUp() }
                )
            }
            composable(Screens.NotificationSettings.screen) {
                NotificationSettingsScreen(
                    onNavigateBack = { navigationController.navigateUp() }
                )
            }
            //TODO: Add Resources Setting

            // Setting page: Redeem Gifts
            composable(Screens.RedeemGifts.screen) {
                Scaffold(
                    bottomBar = { USTBottomBar(navigationController, navViewModel = navViewModel) },
                ) { innerPadding ->
                    Column (modifier
                        .fillMaxSize()
                        .background(USTBlue)
                        .padding(innerPadding)){
                        Redeem(
                            onNavigateBack = { navigationController.navigateUp()},
                            userViewModel = userViewModel,
                            navigationController = navigationController
                        )
                    }
                }
            }


            // Setting page: Support & About
            composable(Screens.Preferences.screen) {
                PreferencesScreen(
                    onNavigateBack = { navigationController.navigateUp() },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange
                )
            }
            composable(Screens.AboutApp.screen) {
                AboutAppScreen(
                    onNavigateBack = { navigationController.navigateUp() }
                )
            }

            // Auth
            composable(Screens.Landing.screen) {
                LandingScreen(
                    onNavigateToLogin = { navigationController.navigate(Screens.Login.screen) },
                    onNavigateToRegister = { navigationController.navigate(Screens.Register.screen) }
                )
            }

            // AI
            composable(Screens.AIDetails.screen) {
                AIDetailsScreen(
                    onNavigateBack = { navigationController.navigateUp() }
                )
            }

            // Document Details
            composable(Screens.DocumentDetails.screen) {
                DocumentDetailsScreen(
                    title = "COMP4521 Lecture 7",
                    onNavigateBack = { navigationController.navigateUp() }
                )
            }

            // Document Upload
            composable(Screens.DocumentUpload.screen) {
                DocumentUploadScreen(
                    onNavigateBack = { navigationController.navigateUp() }
                )
            }

            // Bookmarked document List
            composable(Screens.DocumentBookmarkedList.screen) {
                DocumentListScreen(
                    pageTitle = "bookmarked",
                    onNavigateBack = { navigationController.navigateUp() },
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) }
                )
            }

            // Uploaded document List
            composable(Screens.DocumentUploadedList.screen) {
                DocumentListScreen(
                    pageTitle = "uploaded",
                    onNavigateBack = { navigationController.navigateUp() },
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) }
                )
            }

            // Downloaded document List
            composable(Screens.DocumentDownloadedList.screen) {
                DocumentListScreen(
                    pageTitle = "downloaded",
                    onNavigateBack = { navigationController.navigateUp() },
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) }
                )
            }
            
            // Favorites document List
            composable(Screens.DocumentFavoritesList.screen) {
                DocumentListScreen(
                    pageTitle = "favorites",
                    onNavigateBack = { navigationController.navigateUp() },
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) }
                )
            }
            // Search results document List
            composable(Screens.DocumentSearchResults.screen) {
                DocumentListScreen(
                    pageTitle = "search results",
                    onNavigateBack = { navigationController.navigateUp() }, 
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) }
                )
            }

            // Subject screen
            composable(
                route = Screens.Subject.screen,
                arguments = listOf(
                    navArgument("subject") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val subject = backStackEntry.arguments?.getString("subject") ?: ""
                DocumentListScreen(
                    pageTitle = subject,
                    onNavigateBack = { navigationController.navigateUp() },
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) }
                )
            }
        }
    }

}
