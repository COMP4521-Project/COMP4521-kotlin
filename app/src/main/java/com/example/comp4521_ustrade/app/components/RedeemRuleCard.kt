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
import androidx.compose.ui.res.stringResource
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
                    contentDescription = stringResource(R.string.Settings),
                    modifier = Modifier
                        .padding(2.dp)
                        .size(30.dp),
                )
            }

            Text(
                text = stringResource(R.string.RedeemPrizeRules),
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
                Text(stringResource(R.string.Upload), fontSize = 10.sp)
                Text(stringResource(R.string.Lv1Contributor), fontSize = 10.sp)
                Text(stringResource(R.string.Lv2Contributor), fontSize = 10.sp)
                Text(stringResource(R.string.Lv3Contributor), fontSize = 10.sp)
                Text(stringResource(R.string.EachLevelUnlocks), fontSize = 10.sp)
                Text(
                    stringResource(R.string.GoToSouvenirShop),
                    fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Text(stringResource(R.string.EachAccountCanOnlyRedeemOneGift), fontSize = 10.sp, color = androidx.compose.ui.graphics.Color.Red)
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
                Text(stringResource(R.string.GotIt), fontSize = 12.sp)
            }
        }
    }
}