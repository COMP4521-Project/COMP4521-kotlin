package com.example.comp4521_ustrade.app.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.app.screens.Screens
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import com.example.comp4521_ustrade.ui.theme.USTBlue_light
import com.example.comp4521_ustrade.ui.theme.USTBlue_lightdark
import com.example.comp4521_ustrade.ui.theme.USTWhite
import com.example.comp4521_ustrade.ui.theme.USTgray
import com.example.comp4521_ustrade.ui.theme.USTgray_dark
import com.example.comp4521_ustrade.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun USTTopBar(onOpenDrawer: () -> Unit ,navigationController: androidx.navigation.NavController) {
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }
    var active by remember { mutableStateOf(false) }

    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }
    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    TopAppBar(
        title = {
            SearchBar(
                query = "",
                onQueryChange = { },
                onSearch = {},
                active = false,
                onActiveChange = { navigationController.navigate(Screens.Search.screen)},
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                },
                placeholder = {
                    Text(text = stringResource(R.string.SearchResources), color = Color.Gray)
                },
                colors = SearchBarDefaults.colors(
                    containerColor = (if (isDarkModeEnabled) USTgray_dark else USTgray),
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),


                modifier = Modifier.padding(bottom = 8.dp).height(50.dp).focusable(false),



                ) {}
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = USTWhite,
            actionIconContentColor = USTWhite,
            navigationIconContentColor = USTWhite
        ),
        modifier = Modifier.background(
            if (isDarkModeEnabled) {
                Brush.verticalGradient(
                    colors = listOf(
                        USTBlue_dark, USTBlue_lightdark
                    )
                )
            } else {
                Brush.verticalGradient(
                    colors = listOf(
                        USTBlue, USTBlue_light
                    )
                )
            }
        ),
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ },interactionSource = interactionSource) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.clickable{
                        onOpenDrawer()
                    }
                )
            }
        },
        actions = {

            IconButton(onClick = {
                selected.value = Icons.Default.FavoriteBorder
                navigationController.navigate(Screens.DocumentFavoritesList.screen)
            },interactionSource = interactionSource) {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                )

            }

            IconButton(onClick = {
                selected.value = Icons.Default.Message
                navigationController.navigate(Screens.ChatRoom.screen)
            },interactionSource = interactionSource) {
                Icon(
                    imageVector = Icons.Filled.Message,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

