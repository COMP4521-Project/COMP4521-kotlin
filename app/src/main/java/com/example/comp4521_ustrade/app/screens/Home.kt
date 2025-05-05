package com.example.comp4521_ustrade.app.screens

import AboutAppScreen
import EditPasswordScreen
import EditProfileScreen
import NotificationSettingsScreen
import PreferencesScreen
import ProfilePreviewScreen
import Settings
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ModelTraining
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.ChatbotScreen
import com.example.comp4521_ustrade.app.components.CourseMenu
import com.example.comp4521_ustrade.app.components.DrawerContent
import com.example.comp4521_ustrade.app.components.USTBottomBar
import com.example.comp4521_ustrade.app.components.USTPager
import com.example.comp4521_ustrade.app.components.USTTopBar
import com.example.comp4521_ustrade.app.display.DisplayCourseCards
import com.example.comp4521_ustrade.app.viewmodel.AppSettingsViewModel
import com.example.comp4521_ustrade.app.viewmodel.CourseViewModel
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.auth.AuthViewModel
import com.example.comp4521_ustrade.auth.screens.LandingScreen
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import com.example.comp4521_ustrade.ui.theme.USTBlue_light
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

    val userViewModel: UserViewModel = viewModel { UserViewModel(authViewModel) }
    val navViewModel : NavViewModel = viewModel()

    val courseViewModel :CourseViewModel = viewModel()
    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    val appSettingsViewModel :AppSettingsViewModel = viewModel()
    val isChatbotEnabled by appSettingsViewModel.isChatbotEnabled.collectAsState()

    var showChatbot by remember { mutableStateOf(false) }

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
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(200.dp),
                drawerContainerColor = USTBlue_light,
            ) {
                DrawerContent(
                    navController = navigationController,
                    drawerState = drawerState,
                    courseViewModel = courseViewModel,
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
                    floatingActionButton = {
                        if(isChatbotEnabled) {
                            FloatingActionButton(
                                onClick = { showChatbot = true },
                                containerColor = Color.Transparent,
                                elevation = FloatingActionButtonDefaults.elevation(
                                    defaultElevation = 0.dp,
                                    pressedElevation = 0.dp,
                                    focusedElevation = 0.dp,
                                    hoveredElevation = 0.dp,
                                ),
                                interactionSource = remember { MutableInteractionSource() },
                            )
                            {
                                val composition by rememberLottieComposition(
                                    LottieCompositionSpec.RawRes(
                                        R.raw.bot
                                    )
                                )
                                val progress by animateLottieCompositionAsState(
                                    composition,
                                    iterations = LottieConstants.IterateForever
                                )
                                LottieAnimation(
                                    composition = composition,
                                    progress = { progress },
                                    modifier = Modifier.size(80.dp) // Adjust size as needed
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorScheme.background)
                            .padding(innerPadding)
                    ) {


                        USTPager()
                        CourseMenu(courseViewModel, navigateController=navigationController)

                    }
                }

                if (showChatbot) {
                    AnimatedVisibility(
                        visible = showChatbot,
                        enter = fadeIn() + scaleIn(initialScale = 0.2f),
                        exit = fadeOut() + scaleOut(targetScale = 0.2f)
                    ) {
                        ChatbotScreen(onClose = { showChatbot = false }, navigationController = navigationController)
                    }
                }
            }
            composable(Screens.Download.screen) {
                DocumentListScreen(
                pageTitle = "downloaded",
                onNavigateBack = { navigationController.navigateUp() },
                onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) },
                    navViewModel = navViewModel
            )}
            composable(Screens.Profile.screen) {
                  Profile(
                        navigationController = navigationController,
                        authViewModel = authViewModel,
                        userViewModel = userViewModel,
                        navViewModel = navViewModel
                  )
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
                navViewModel = navViewModel
            ) }
            composable(Screens.Search.screen) {
                Scaffold(
                    topBar = { Search() },
                    bottomBar = { USTBottomBar(navigationController, navViewModel = navViewModel) },
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorScheme.background)
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
                    onNavigateBack = { navigationController.navigateUp() },
                    userViewModel = userViewModel
                )
            }
            composable(Screens.EditPassword.screen) {
                EditPasswordScreen(
                    onNavigateBack = { navigationController.navigateUp() },
                    authViewModel = authViewModel
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
                        Redeem(
                            onNavigateBack = { navigationController.navigateUp()},
                            userViewModel = userViewModel,
                            navigationController = navigationController,
                            navViewModel = navViewModel
                        )

            }


            // Setting page: Support & About
            composable(Screens.Preferences.screen) {
                PreferencesScreen(
                    onNavigateBack = { navigationController.navigateUp() },
                    isDarkTheme = isDarkTheme,
                    onThemeChange = onThemeChange,
                    appSettingsViewModel = appSettingsViewModel,
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
            composable(
                route = Screens.DocumentDetails.screen + "/{documentId}",
                arguments = listOf(navArgument("documentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val documentId = backStackEntry.arguments?.getString("documentId") ?: ""
                DocumentDetailsScreen(
                    documentId = documentId,
                    onNavigateBack = { navigationController.navigateUp() }
                )
            }

            // Document Upload
            composable(Screens.DocumentUpload.screen) {
                DocumentUploadScreen(
                    onNavigateBack = {
                        navigationController.navigateUp()} ,
                    navViewModel = navViewModel,
                    userViewModel = userViewModel
                )
            }

            // Bookmarked document List
            composable(Screens.DocumentBookmarkedList.screen) {
                DocumentListScreen(
                    pageTitle = "bookmarked",
                    onNavigateBack = { navigationController.navigateUp() },
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) },
                    navViewModel = navViewModel
                )
            }

            // Uploaded document List
            composable(Screens.DocumentUploadedList.screen) {
                DocumentListScreen(
                    pageTitle = "uploaded",
                    onNavigateBack = { navigationController.navigateUp() },
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) },
                    navViewModel = navViewModel
                )
            }

            // Downloaded document List
            composable(Screens.DocumentDownloadedList.screen) {
                DocumentListScreen(
                    pageTitle = "downloaded",
                    onNavigateBack = { navigationController.navigateUp() },
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) },
                    navViewModel = navViewModel
                )
            }
            
            // Favorites document List
            composable(Screens.DocumentFavoritesList.screen) {
                DocumentListScreen(
                    pageTitle = "favorites",
                    onNavigateBack = { navigationController.navigateUp() },
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) },
                    navViewModel = navViewModel
                )
            }
            // Search results document List
            composable(Screens.DocumentSearchResults.screen) {
                DocumentListScreen(
                    pageTitle = "search results",
                    onNavigateBack = { navigationController.navigateUp() }, 
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) },
                    navViewModel = navViewModel
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
                    onDocumentClick = { navigationController.navigate(Screens.DocumentDetails.screen) },
                    navViewModel = navViewModel
                )
            }
        }
    }

}
