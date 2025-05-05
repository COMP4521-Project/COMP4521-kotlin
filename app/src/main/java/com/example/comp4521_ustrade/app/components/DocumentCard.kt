package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
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
            // TODO: Image, please hard code the image for now
            Image(
                painter = painterResource(R.drawable.comp1),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            // AsyncImage(
            //     model = document.imageUrl,
            //     contentDescription = null,
            //     modifier = Modifier
            //         .size(80.dp)
            //         .clip(RoundedCornerShape(4.dp))
            // )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
        }
    }
}

