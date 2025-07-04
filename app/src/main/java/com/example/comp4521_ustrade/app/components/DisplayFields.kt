package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.app.models.DisplayFieldItem

@Composable
fun DisplayFields(
    fields: List<DisplayFieldItem>, 
    modifier: Modifier = Modifier,
    defaultFontSize: Int = 16,  // Added defaultFontSize parameter
    defaultTextStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium  // Added defaultTextStyle parameter
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        fields.forEach { field ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = field.title,
                    fontSize = (defaultFontSize).sp,  // Use field-specific font size
                    style = defaultTextStyle,  // Use field-specific style or default
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = field.value,
                    fontSize = (defaultFontSize).sp,  // Use field-specific font size
                    style = (defaultTextStyle).copy(  // Use field-specific style or default
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .weight(2f)
                        .then(
                            if (field.onClick != null) {
                                Modifier.clickable { field.onClick.invoke() }
                            } else {
                                Modifier
                            }
                        ),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}