package com.example.comp4521_ustrade.app.components

import android.graphics.pdf.PdfDocument.Page
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R


//to display advertisement

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun USTPager() {

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
        modifier = modifier
            .height(200.dp)
            .wrapContentSize(align = Alignment.Center)
            .padding(0.dp)
            .fillMaxSize()
            .background(Color.White)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(0.dp),
//            pageSize = if(pagerState.currentPage < 3) PageSize.Fixed(410.dp) else PageSize.Fill,
            contentPadding = PaddingValues(0.dp),
        ) { currentPage ->
                Banner(images[currentPage], modifier, currentPage, pagerState)
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
        modifier = modifier
            .padding(2.dp)
            .size(size.value)
            .clip(CircleShape)
            .background(if (isSelected) Color(0xFFB9B9B9) else Color(0xFFC9C9C9))
    )
}


@Composable
fun Banner(imagesRes: Int, modifier: Modifier = Modifier, page:Int, pagerState:PagerState) {
    Card(
        modifier = Modifier.padding(0.dp).fillMaxSize().offset(x = -3.dp, y = 0.dp).graphicsLayer(scaleX = 1.05f, scaleY = 1f),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Image(
            painter = painterResource(id = imagesRes),
            contentDescription = "Image $page",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillWidth

        )
    }
}

