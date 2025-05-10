package com.example.comp4521_ustrade.app.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.ConfirmRedeemSheet
import com.example.comp4521_ustrade.app.components.RedeemCard
import com.example.comp4521_ustrade.app.components.RedeemDialog
import com.example.comp4521_ustrade.app.components.USTBottomBar
import com.example.comp4521_ustrade.app.display.DisplayPrize
import com.example.comp4521_ustrade.app.display.displayProfileCard
import com.example.comp4521_ustrade.app.models.Prize
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import com.example.comp4521_ustrade.ui.theme.USTWhite
import com.example.comp4521_ustrade.ui.theme.USTgray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Redeem(modifier: Modifier = Modifier,
           onNavigateBack: () -> Unit,
           userViewModel : UserViewModel,
           navigationController: NavController,
           navViewModel: NavViewModel) {
    val isButtonEnabled = remember { mutableStateOf(true) }
    val isDialogVisible = remember { mutableStateOf(false) }
    val showBottomSheet = remember { mutableStateOf(false) }

    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    val selectedPrize by userViewModel.selectedPrize.observeAsState()
    val confirmPrize by userViewModel.confirmPrize.observeAsState()

    // Load saved prize on first launch
    LaunchedEffect(Unit) {
        val savedPrizeIcon = sharedPreferences.getInt("selected_prize_icon", -1)
        if (savedPrizeIcon != -1) {
            val prize = Prize(savedPrizeIcon)
            userViewModel.setConfirmedPrize(prize)
        }
    }

    // Save prize when it's confirmed
    LaunchedEffect(confirmPrize) {
        if (confirmPrize != null) {
            sharedPreferences.edit().apply {
                putInt("selected_prize_icon", confirmPrize!!.icon)
                apply()
            }
        }
    }

    Scaffold(
        bottomBar = { USTBottomBar(navigationController, navViewModel = navViewModel) },
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .background(if (isDarkModeEnabled) USTBlue_dark else USTBlue)
                .padding(innerPadding)
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(colorScheme.background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(if (isDarkModeEnabled) USTBlue_dark else USTBlue)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, top = 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row() {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = USTWhite,
                                    modifier = Modifier.offset(0.dp, -4.dp)
                                )
                            }
                        }
                        Row() {
                            IconButton(onClick = { isDialogVisible.value = true }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info Icon",
                                    tint = USTWhite
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(top = 43.dp, start = 16.dp)) {
                        Text(text = stringResource(R.string.UploadSources), fontSize = 20.sp, color = USTWhite)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.LevelUpAndRedeemRewards),
                            fontSize = 20.sp,
                            color = USTWhite
                        )
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .width(420.dp)
                            .height(300.dp)
                            .padding(start = 16.dp, top = 120.dp, end = 16.dp),
                    ) {
                        RedeemCard(userViewModel = userViewModel)
                    }
                    if (confirmPrize != null) {
                        Text(
                            text = stringResource(R.string.YouHaveSelectedYourPrize),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = 16.dp),
                        )
                    }
                    DisplayPrize(
                        userViewModel = userViewModel,
                        onPrizeClick = {
                            showBottomSheet.value = true
                        })
                }


                // Show the info dialog if the state is true
                if (isDialogVisible.value) {
                    RedeemDialog(onDismissRequest = {
                        isDialogVisible.value = false
                        navigationController.navigate(Screens.RedeemGifts.screen)
                    })
                }


                //Show bottom sheet if the state is true
                if (showBottomSheet.value) {
                    ConfirmRedeemSheet(modifier, selectedPrize, showBottomSheet, userViewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRedeem() {
//    Redeem(onNavigateBack = {})
}
