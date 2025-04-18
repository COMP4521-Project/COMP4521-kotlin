package com.example.comp4521_ustrade.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.app.components.RedeemCard
import com.example.comp4521_ustrade.app.display.DisplayPrize
import com.example.comp4521_ustrade.app.display.displayProfileCard
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTWhite

@Composable
fun Redeem(modifier: Modifier = Modifier,    onNavigateBack: () -> Unit) {
    val isButtonEnabled = remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(USTBlue)
        ) {
            Column (modifier = Modifier.padding(top = 36.dp, start = 16.dp)){
                Text(text = "Upload sources", fontSize = 24.sp, color = USTWhite)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Level up and redeem rewards", fontSize = 24.sp, color = USTWhite)
            }
        }

        Column (modifier = Modifier.fillMaxWidth()){
            Box(
                modifier = Modifier
                    .width(420.dp).height(300.dp)
                    .padding(start = 16.dp, top = 120.dp, end = 16.dp),
            ) {
                RedeemCard()
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(onClick = {  },
                    contentPadding = contentPadding(start = 50.dp,end = 50.dp, top = 5.dp, bottom = 5.dp),
                    colors = ButtonColors(
                        containerColor = USTBlue,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.DarkGray
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )

                { Text(text = "Redeem") }
            }
            DisplayPrize()
        }



    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRedeem() {
    Redeem(onNavigateBack = {})
}
