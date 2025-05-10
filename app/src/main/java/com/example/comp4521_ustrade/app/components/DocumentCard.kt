package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.comp4521_ustrade.ui.theme.USTgray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentCard(
    document: com.example.comp4521_ustrade.app.data.dao.Document,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, USTgray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
             AsyncImage(
                 model = document.thumbnailUrl,
                 contentDescription = null,
                 modifier = Modifier
                     .size(80.dp)
                     .clip(RoundedCornerShape(4.dp))
             )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = document.course,
                    style = MaterialTheme.typography.titleMedium
                )
//                Text(
//                    text = document.subtitle,
//                    style = MaterialTheme.typography.bodyMedium
//                )
                Text(
                    text = document.year + " " + document.semester,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = document.title,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Like count display
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Likes",
                    tint = Color.Red,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = document.like_count.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

