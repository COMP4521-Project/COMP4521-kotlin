package com.example.comp4521_ustrade.app.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.ShortCutCard
import com.example.comp4521_ustrade.app.models.ShortCutCardItem
import com.example.comp4521_ustrade.auth.AuthViewModel

@Composable
fun DisplayShortCutCards(
    navigateController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {

    val shortcutList = listOf(
        ShortCutCardItem(R.drawable.bookmark, stringResource(R.string.Bookmark)),
        ShortCutCardItem(R.drawable.upload, stringResource(R.string.Upload)),
        ShortCutCardItem(R.drawable.redeem, stringResource(R.string.Redeem)),
        ShortCutCardItem(R.drawable.download, stringResource(R.string.Download)),
        ShortCutCardItem(R.drawable.bot, stringResource(R.string.AI)),
        ShortCutCardItem(R.drawable.preview, stringResource(R.string.Preview)),
        ShortCutCardItem(R.drawable.preference, stringResource(R.string.Preference)),
        ShortCutCardItem(R.drawable.logout, stringResource(R.string.Logout))
    )

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(start=8.dp),
        contentAlignment = Alignment.Center

        ) {
        LazyHorizontalGrid (
            rows = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize() ,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
        ) {
            items(shortcutList.size) { index ->
                ShortCutCard(ShortCutCardItem = shortcutList[index], navigateController = navigateController, authViewModel = authViewModel)
            }
        }
    }
}

