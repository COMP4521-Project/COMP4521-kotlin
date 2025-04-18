package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.app.models.ProfileCardData
import com.example.comp4521_ustrade.ui.theme.Badges

@Composable
fun ProfileCard(modifier: Modifier = Modifier, ProfileCardData : ProfileCardData) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
    ) {
        Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically)
        {
            Image(
                painter = painterResource(id = ProfileCardData.profilePicture),
                contentDescription = "Profile Picture",
                modifier = modifier
                    .weight(2f)
                    .size(100.dp)
                    .padding(start = 8.dp, end = 16.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Column(modifier = modifier.weight(3f), verticalArrangement = Arrangement.SpaceAround) {
                Text(text = ProfileCardData.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier.padding(2.dp))
                Surface(
                    color = Badges,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "Lv.1 Contributor",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.Black
                    )
                }
            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.IosShare,
                    contentDescription = "Share",
                    modifier = modifier.weight(1f),
                    tint = Color.Gray
                )
            }


        }

        Row(modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${ProfileCardData.upload_count}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "Uploads", fontSize = 16.sp, modifier = modifier.padding(top = 8.dp))
            }
            VerticalDivider(
                modifier = modifier
                    .padding(8.dp)
                    .height(50.dp),
                thickness = 1.dp,
                color = androidx.compose.ui.graphics.Color.Gray
            )
            Column(modifier = modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${ProfileCardData.download_count}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = "Downloads", fontSize = 16.sp, modifier = modifier.padding(top = 8.dp))
            }
        }
    }
}
