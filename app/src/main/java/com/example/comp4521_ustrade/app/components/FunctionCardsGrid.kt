package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class FunctionCardData(
    val icon: ImageVector,
    val title: String
)

@Composable
fun FunctionCardsGrid(
    functionCards: List<FunctionCardData>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        functionCards.forEach { cardData ->
            FunctionCard(
                icon = cardData.icon,
                title = cardData.title,
                onClick = { /* handle click here */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

