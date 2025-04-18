package com.example.comp4521_ustrade.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.app.components.ContributorCard
import com.example.comp4521_ustrade.app.display.DisplayShortCutCards
import com.example.comp4521_ustrade.app.display.displayProfileCard
import com.example.comp4521_ustrade.auth.AuthViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTWhite

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    navigationController: NavController,
    authViewModel: AuthViewModel
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(USTBlue)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Row() {
                    Text(
                        text = "@hachiware103",
                        fontSize =20.sp,
                        fontWeight = FontWeight.Bold,
                        color = USTWhite
                    )
                }
                Row() {
                    IconButton(
                        onClick = { 
                            navigationController.navigate(Screens.Settings.screen)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = USTWhite
                        )
                    }
                    IconButton(onClick = { /* TODO: Action 2 */ }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Icon 2",
                            tint = USTWhite
                        )
                    }
                    IconButton(onClick = { /* TODO: Action 3 */ }) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Icon 3",
                            tint = USTWhite
                        )
                    }
                }
            }
        }
        Column {
            Box(
                modifier = Modifier
                    .width(420.dp).height(280.dp)
                    .padding(start = 16.dp, top = 80.dp, end = 16.dp),
            ) {
                displayProfileCard()
            }

            DisplayShortCutCards(navigateController = navigationController, authViewModel)

            Box(
                modifier = Modifier
                    .width(420.dp).fillMaxHeight()
                    .padding(start = 20.dp, end = 20.dp),
            ){
                ContributorCard()
            }

        }
    }
}