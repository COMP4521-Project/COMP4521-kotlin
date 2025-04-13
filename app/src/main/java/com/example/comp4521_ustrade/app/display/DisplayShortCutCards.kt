package com.example.comp4521_ustrade.app.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.ShortCutCard
import com.example.comp4521_ustrade.app.models.ShortCutCardItem

@Composable
fun DisplayShortCutCards(modifier: Modifier = Modifier) {

    val shortcutList = listOf(
        ShortCutCardItem(R.drawable.bookmark, "Bookmark"),
        ShortCutCardItem(R.drawable.upload, "Upload"),
        ShortCutCardItem(R.drawable.redeem, "Redeem"),
        ShortCutCardItem(R.drawable.download, "Download"),
        ShortCutCardItem(R.drawable.bot, "AI"),
        ShortCutCardItem(R.drawable.preview, "Preview"),
        ShortCutCardItem(R.drawable.preference, "Preference"),
        ShortCutCardItem(R.drawable.logout, "Logout"),
    )


    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        LazyHorizontalGrid (
            rows = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
        ) {
            items(shortcutList.size) { index ->
                ShortCutCard(ShortCutCardItem = shortcutList[index]
                )
            }
        }
    }


}

