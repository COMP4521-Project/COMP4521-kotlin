package com.example.comp4521_ustrade.app.components

import android.graphics.drawable.Icon
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.models.ProfileCardData
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel

@Composable
fun RedeemCard(modifier: Modifier = Modifier, userViewModel : UserViewModel) {

    val uploadCount = userViewModel.uploadCount.observeAsState().value

    var level = 0;

    var progress by remember { mutableStateOf(0f) }



    if (uploadCount != null && uploadCount <= 20) {
            progress = uploadCount / 20.0f
        }

    if (uploadCount != null) {
        if (uploadCount >= 5 && uploadCount < 12) {
            level = 1
        } else if (uploadCount >= 12 && uploadCount < 20) {
            level = 2
        } else if (uploadCount >= 20) {
            level = 3
            progress = 1f
        }
    }



    val size = progress.let {
        animateFloatAsState(
            targetValue = it,
            tween(
                durationMillis = 1000,
                delayMillis = 100,
                easing = LinearOutSlowInEasing
            )
        ).value
    } ?: 0f

    Card(
        colors = CardDefaults.cardColors(containerColor = colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(0.dp)
    ) {
            Spacer(modifier.padding(2.dp))
            Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically)
            {
                Image(
                    painter = painterResource(id = R.drawable.trophy),
                    contentDescription = "Trophy Picture",
                    modifier = modifier
                        .weight(1f)
                        .size(70.dp)
                        .padding(start = 8.dp, end = 16.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape),
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit
                )

                Column(modifier = modifier.weight(3f), verticalArrangement = Arrangement.SpaceAround) {
                    Text(text = "Contributor Lv.$level", fontSize = 26.sp, fontWeight = Bold)
                    Spacer(modifier.padding(4.dp))
                    Text(text = "You have uploaded $uploadCount documents", fontSize = 14.sp)
                }
            }
            Spacer(modifier.padding(2.dp))
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
                        .background(Color(0xFF90EE90))
                        .animateContentSize()
                )
            }
            Spacer(modifier.padding(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .size(20.dp)
                        .padding(start = 8.dp),
                    painter = painterResource(id = R.drawable.award),
                    contentDescription = "reward"
                )

                if (level == 0) {
                    Text(text = "Level up to redeem your first prize", fontSize = 12.sp, modifier = modifier.padding(start = 8.dp).weight(6f))
                } else {
                    Text(text = "Pick a prize below", fontSize = 12.sp, modifier = modifier.padding(start = 8.dp).weight(6f))
                }
            }
        Spacer(modifier.padding(2.dp))
        }

    }

@Preview(showBackground = true)
@Composable
fun PreviewRedeemCard() {
//    RedeemCard()
}