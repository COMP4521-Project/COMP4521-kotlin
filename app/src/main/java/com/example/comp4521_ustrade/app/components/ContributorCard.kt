package com.example.comp4521_ustrade.app.components

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.screens.Screens
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.Badges
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark

@Composable
fun ContributorCard(modifier: Modifier = Modifier, userViewModel : UserViewModel, navigationController : NavController) {

    val uploadCountString = userViewModel.upload_count.observeAsState().value
    val uploadCount = uploadCountString?.toIntOrNull() ?: 0

    var progress by remember { mutableFloatStateOf(0f) }

    if (uploadCount <= 20) {
        progress = uploadCount / 20.0f
    }
    else {
        progress = 1f
    }

    val size by animateFloatAsState(
        targetValue = progress,
        tween(
            durationMillis = 1000,
            delayMillis = 100,
            easing = LinearOutSlowInEasing
        )
    )

    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.LightGray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),){
        Column {
            Box(modifier= Modifier.fillMaxWidth().padding(top = 8.dp), contentAlignment = Alignment.TopEnd) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.weight(1f).padding(start = 50.dp),
                        painter = painterResource(id = R.drawable.reward1),
                        contentDescription = "reward"
                    )
                    Image(
                        modifier = Modifier.weight(1f).padding(start = 50.dp),
                        painter = painterResource(id = R.drawable.reward1),
                        contentDescription = "reward"
                    )
                    Image(
                        modifier = Modifier.weight(1f).padding(end=8.dp),
                        alignment = androidx.compose.ui.Alignment.TopEnd,
                        painter = painterResource(id = R.drawable.reward1),
                        contentDescription = "reward"
                    )
                }
            }
            //for the text above the progressBar
            Row(
                modifier = Modifier
                    .widthIn(min = 30.dp)
                    .fillMaxWidth(size),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$uploadCount")
            }
            //Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(17.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color.LightGray)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(size)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(9.dp))
                        .background(Badges)
                        .animateContentSize()
                )
            }
            //text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                if (uploadCount != null) {
                    if(uploadCount >= 20) {
                        Column {
                            Text(
                                text = stringResource(R.string.MaxLevel),
                            )
                        }
                    } else {
                        if (uploadCount >= 12){
                            Text(
                                text = stringResource(R.string.UploadMore, (20 - uploadCount)),
                            )
                        }else if (uploadCount >= 5){
                            Text(
                                text = stringResource(R.string.UploadMore2, (12 - uploadCount)),
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.UploadMore3, (5 - uploadCount)),
                            )
                        }
                    }
                }
            }

            Row{
                Button(onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                    )) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = stringResource(R.string.LearnMore),
                            modifier = Modifier.clickable {navigationController.navigate(Screens.RedeemGifts.screen) },
                            color = (if (isDarkModeEnabled) USTBlue else USTBlue_dark))
                        Icon(
                            modifier = Modifier.padding(start = 5.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "",
                            tint = (if (isDarkModeEnabled) USTBlue else USTBlue_dark)
                        )
                    }
                }

                //test
//                Box(
//                ) {
//                    Button(onClick = {
//                        if (uploadCount != null) {
//                            userViewModel.addUploadCount()
//
//                        }
//                    }) {
//                        Text(text = "Upload test")
//                    }
//                }
            }
        }

    }
}
