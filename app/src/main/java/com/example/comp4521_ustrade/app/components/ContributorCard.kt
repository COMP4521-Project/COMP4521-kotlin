package com.example.comp4521_ustrade.app.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R

@Composable
fun ContributorCard(modifier: Modifier = Modifier) {
    var uploadCount : Int by remember {
        mutableStateOf(0)
    }

    var progress by remember { mutableStateOf(0f) }

    progress = uploadCount / 20.0f

    val size by animateFloatAsState(
        targetValue = progress,
        tween(
            durationMillis = 1000,
            delayMillis = 100,
            easing = LinearOutSlowInEasing
        )
    )

    Card(        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
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
                        .background(Color(0xFF90EE90))
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
                if(uploadCount == 20) {
                    Column {
                        Text(
                            text = "Congratulations! You have reached the max level.",
                            color = Color.Black,
                        )
                    }
                } else if (uploadCount >= 12){
                    Text(
                        text = "Upload ${20 - uploadCount} more documents to become Lv.3 contributor",
                        color = Color.Black,
                    )
                }else if (uploadCount >= 5){
                    Text(
                        text = "Upload ${12 - uploadCount} more documents to to become Lv.2 contributor",
                        color = Color.Black,
                    )
                } else {
                    Text(
                        text = "Upload ${5 - uploadCount} more documents to to become Lv.1 contributor",
                        color = Color.Black,
                    )
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
                        Text(text = "Learn more", color = Color.Blue)
                        Icon(
                            modifier = Modifier.padding(start = 5.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "forward",
                            tint = Color.Blue
                        )
                    }
                }

                //test
                Box(
                ) {
                    Button(onClick = {
                        if (uploadCount < 20) {
                            uploadCount++
                        }
                    }) {
                        Text(text = "Upload test")
                    }
                }
            }
        }

    }
}
