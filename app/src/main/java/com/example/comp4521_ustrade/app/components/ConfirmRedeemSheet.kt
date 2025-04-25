package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.app.models.Prize
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTWhite
import com.example.comp4521_ustrade.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmRedeemSheet(modifier: Modifier = Modifier,
                       selectedPrize: Prize?,
                       showBottomSheet: MutableState<Boolean>,
                       userViewModel: UserViewModel
) {
    ModalBottomSheet(
        onDismissRequest = { showBottomSheet.value = false }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.RedeemThisPrize), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            selectedPrize?.let { prize ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 0.dp),
                    contentAlignment = Alignment.Center

                ) {
                    Image(
                        painter = painterResource(id = prize.icon),
                        contentDescription = "Prize Icon",
                        modifier = Modifier.height(256.dp),
                        contentScale = ContentScale.Crop
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            showBottomSheet.value = false
                            userViewModel.setConfirmedPrize(prize)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = USTBlue,
                            contentColor = USTWhite

                        ),
                    ) {
                        Text(text = stringResource(R.string.Confirm))
                    }
                    Button(
                        onClick = {
                            showBottomSheet.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = USTWhite
                        ),
                    ) {
                        Text(text = stringResource(R.string.Cancel))
                    }
                }
            }

        }
    }
}