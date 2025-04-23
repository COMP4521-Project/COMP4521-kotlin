package com.example.comp4521_ustrade.app.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.comp4521_ustrade.app.screens.Screens
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import com.example.comp4521_ustrade.ui.theme.USTBlue_light
import com.example.comp4521_ustrade.ui.theme.USTBlue_lightdark
import com.example.comp4521_ustrade.ui.theme.USTWhite


@Composable
fun USTBottomBar(navigationController: NavController = rememberNavController(), navViewModel: NavViewModel) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }
    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    val selectedScreen = navViewModel.selectedScreen.observeAsState(Screens.Home)

    BottomAppBar(containerColor = Color.Transparent,
        modifier = Modifier.background(
            if(isDarkModeEnabled) {
                Brush.verticalGradient(
                    colors = listOf(
                        USTBlue_lightdark, USTBlue_dark
                    )
                )
            }else{
                Brush.verticalGradient(
                    colors = listOf(
                        USTBlue_light, USTBlue
                    )
                )
            }
        )) {
        IconButton(
            onClick = {
                navViewModel.setSelectedScreen(Screens.Home)
                navigationController.navigate(Screens.Home.screen)
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.padding(2.dp).size(32.dp),
                tint = if (selectedScreen.value == Screens.Home) USTWhite else Color.Gray
            )
        }

        IconButton(
            onClick = {
                navViewModel.setSelectedScreen(Screens.Download)
                navigationController.navigate(Screens.Download.screen)
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = null,
                modifier = Modifier.padding(2.dp).size(32.dp),
                tint = if (selectedScreen.value == Screens.Download) USTWhite else Color.Gray
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                containerColor = Color.Red,
                onClick = { navigationController.navigate(Screens.DocumentUpload.screen) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(2.dp).size(32.dp),
                    tint = USTWhite
                )
            }
        }

        IconButton(
            onClick = {
                navViewModel.setSelectedScreen(Screens.Notification)
                navigationController.navigate(Screens.Notification.screen)
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                modifier = Modifier.padding(2.dp).size(32.dp),
                tint = if (selectedScreen.value == Screens.Notification) USTWhite else Color.Gray
            )
        }

        IconButton(
            onClick = {
                navViewModel.setSelectedScreen(Screens.Profile)
                navigationController.navigate(Screens.Profile.screen)
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.padding(2.dp).size(32.dp),
                tint = if (selectedScreen.value == Screens.Profile) USTWhite else Color.Gray
            )
        }
    }
}