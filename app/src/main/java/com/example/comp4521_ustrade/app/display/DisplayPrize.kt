package com.example.comp4521_ustrade.app.display

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.CourseCard
import com.example.comp4521_ustrade.app.components.GreyPrizeCard
import com.example.comp4521_ustrade.app.components.PrizeCard
import com.example.comp4521_ustrade.app.models.Prize
import com.google.android.play.integrity.internal.c

@Composable
fun DisplayPrize(modifier: Modifier = Modifier) {
    val prizeList = listOf(
        Prize(R.drawable.prize1),
        Prize(R.drawable.prize2),
        Prize(R.drawable.prize3),
        Prize(R.drawable.prize4),
        Prize(R.drawable.prize5),
        Prize(R.drawable.prize6))

    var level = 1
    var selectedPrize by remember { mutableStateOf<Prize?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        when (level) {
            1 -> {
                items(prizeList.size) { index: Int ->
                    if (index < 2) {
                        val prize = prizeList[index]
                        PrizeCard(
                            prize = prize,
                            isSelected = selectedPrize == prize,
                            onSelect = { selectedPrize = prize }
                        )
                    }
                    else {
                        GreyPrizeCard(Prize = prizeList[index])
                    }
                }
            }
            2 -> {
                items(prizeList.size) { index: Int ->
                        if (index < 4) {
                            val prize = prizeList[index]
                            PrizeCard(
                                prize = prize,
                                isSelected = selectedPrize == prize,
                                onSelect = { selectedPrize = prize }
                            )
                        }
                        else {
                            GreyPrizeCard(Prize = prizeList[index])
                        }
                    }
                }

            3 -> {
                items(prizeList.size) { index: Int ->
                    val prize = prizeList[index]
                    PrizeCard(
                        prize = prize,
                        isSelected = selectedPrize == prize,
                        onSelect = { selectedPrize = prize }
                    )
                }
            }
        }
    }
}