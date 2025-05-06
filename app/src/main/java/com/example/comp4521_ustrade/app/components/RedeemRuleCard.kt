package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTWhite


@Composable
fun RedeemDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = { onDismissRequest() }) {
        DialogContent(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
        )
    }
}

@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(){
                Image(
                    painter = painterResource(id = R.drawable.guide),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .padding(2.dp)
                        .size(30.dp),
                )
            }

            Text(
                text = "Redeem prize rules",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp),
            )


            // Progress information
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Upload", fontSize = 10.sp)
                Text("• 5 documents → Lv.1 contributor", fontSize = 10.sp)
                Text("• 12 documents → Lv.2 contributor", fontSize = 10.sp)
                Text("• 20 documents → Lv.3 contributor", fontSize = 10.sp)
                Text("Each level unlocks 2 prize options to pick", fontSize = 10.sp)
                Text(
                    "Go to the souvenir shop to collect your prize after you press the confirm Button",
                    fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Text("* Each account can only redeem one gift at most", fontSize = 10.sp, color = androidx.compose.ui.graphics.Color.Red)
            }

            // Action button
            TextButton(
                onClick = { onDismissRequest() },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.CenterHorizontally),
                colors = buttonColors(
                    containerColor = USTBlue,
                    contentColor = USTWhite
                )
            ) {
                Text("Got it", fontSize = 12.sp)
            }
        }
    }
}