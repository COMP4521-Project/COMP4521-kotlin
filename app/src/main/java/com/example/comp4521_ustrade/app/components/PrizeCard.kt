package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.app.models.Prize

@Composable
fun PrizeCard(
    modifier: Modifier = Modifier,
    prize: Prize,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Image(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .graphicsLayer(alpha = 1f)
            .clickable { onSelect() }
            .then(if (isSelected)
                {
                    Modifier
                        .border(3.dp, Color.Green, shape = RoundedCornerShape(3.dp))
                        .shadow(15.dp, shape = RoundedCornerShape(3.dp))
                }else {
             Modifier}),
        painter = painterResource(id = prize.icon),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun GreyPrizeCard(modifier: Modifier = Modifier, Prize : Prize) {
    Image(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .graphicsLayer(alpha = 0.5f),
        painter = painterResource(id = Prize.icon),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}