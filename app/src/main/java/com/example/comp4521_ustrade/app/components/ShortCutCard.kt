package com.example.comp4521_ustrade.app.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.app.models.ShortCutCardItem

@Composable
fun ShortCutCard(ShortCutCardItem: ShortCutCardItem, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.size(85.dp).clickable {
            Toast.makeText(
                context,
                "Clicked: ${ShortCutCardItem.title}",
                Toast.LENGTH_SHORT
            ).show()
        },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp),
                contentDescription = "shortcut_item",
                painter = painterResource(id = ShortCutCardItem.icon),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ShortCutCardItem.title,
                fontSize = 12.sp,
            )
        }
    }
}
