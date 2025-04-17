package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DisplayOnlyFieldItem(
    val title: String,
    val value: String
)

@Composable
fun DisplayOnlyField(
    field: DisplayOnlyFieldItem,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = field.title,
            fontSize = 14.sp,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = field.value,
            fontSize = 16.sp,
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun DisplayOnlyFields(
    fields: List<DisplayOnlyFieldItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        fields.forEach { field ->
            DisplayOnlyField(field = field)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
} 