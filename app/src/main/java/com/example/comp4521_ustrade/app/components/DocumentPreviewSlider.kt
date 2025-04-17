package com.example.comp4521_ustrade.app.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.ui.theme.USTBlue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DocumentPreviewSlider(
    images: List<Int>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { images.size }
    
    // Calculate progress for the progress bar
    val progress by animateFloatAsState(
        targetValue = (pagerState.currentPage.toFloat() / (images.size - 1).coerceAtLeast(1)),
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = modifier
            .background(Color.White)
            .padding(0.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { currentPage ->
            Card(
                modifier = Modifier.padding(0.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Image(
                    painter = painterResource(id = images[currentPage]),
                    contentDescription = "Preview page ${currentPage + 1}",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Bottom container for indicators and progress bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 32.dp)
            ) {
                // Background track
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.LightGray.copy(alpha = 0.5f))
                )
                // Progress indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(USTBlue)
                )
            }
        }
    }
} 