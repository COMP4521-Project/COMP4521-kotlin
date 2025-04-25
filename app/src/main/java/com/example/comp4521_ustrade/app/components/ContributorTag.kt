package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.ui.theme.Badges
import com.example.comp4521_ustrade.ui.theme.Badges2
import com.example.comp4521_ustrade.ui.theme.Badges3
import com.example.comp4521_ustrade.ui.theme.USTWhite
import com.example.comp4521_ustrade.R

@Composable
fun ContributorTag(modifier: Modifier = Modifier, level : Int) {
    when (level) {
        1 ->
            Surface(
                color = Badges,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(R.string.Lv1Contributor),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.Black
                )
            }

        2 ->
            Surface(
                color = Badges2,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(R.string.Lv2Contributor),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.Black
                )
            }

        3 ->
            Surface(
                color = Badges3,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = stringResource(R.string.Lv3Contributor),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = USTWhite
                )
            }

        else -> {}
    }
}