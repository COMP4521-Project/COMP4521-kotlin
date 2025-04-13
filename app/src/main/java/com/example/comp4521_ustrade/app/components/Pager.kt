package com.example.comp4521_ustrade.app.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R


//to display advertisement

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pager() {

    val images = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3,
    )

    val pagerState = rememberPagerState { images.size }

    val scope = rememberCoroutineScope()


    val modifier = Modifier
        .padding(0.dp)

    Box(
        modifier = modifier.height(200.dp)
            .background(Color.White)
            .wrapContentSize(align = Alignment.Center)
            .padding(0.dp)

    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(0.dp)
        ) { currentPage ->
            Card(
                modifier = Modifier
                    .padding(0.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = painterResource(id = images[currentPage]),
                    contentDescription = "Image $currentPage",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop

                )
            }
        }

        PageIndictator(
            count = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(4.dp)
        )
    }
}

@Composable
fun PageIndictator(count: Int, currentPage: Int, modifier: Modifier) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(0.dp)
    ){
        repeat(count) {
            IndicatorDots(isSelected = it == currentPage, modifier = modifier)
        }
    }
}

@Composable
fun IndicatorDots(isSelected: Boolean, modifier: Modifier) {
    val size = animateDpAsState(targetValue = if(isSelected) 10.dp else 6.dp)
    Box(
        modifier = modifier.padding(2.dp)
            .size(size.value)
            .clip(CircleShape)
            .background(if (isSelected) Color(0xFFB9B9B9) else Color(0xFFC9C9C9) )
    )
}
