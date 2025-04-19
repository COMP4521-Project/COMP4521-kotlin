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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.app.models.ShortCutCardItem
import com.example.comp4521_ustrade.app.screens.Screens
import com.example.comp4521_ustrade.auth.AuthViewModel

@Composable
fun ShortCutCard(
    ShortCutCardItem: ShortCutCardItem,
    navigateController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.size(76.dp).clickable {
            Toast.makeText(
                context,
                "Clicked: ${ShortCutCardItem.title}",
                Toast.LENGTH_SHORT
            ).show()
            when (ShortCutCardItem.title) {
                "AI" -> navigateController.navigate(Screens.AIDetails.screen)
                "Preference" -> navigateController.navigate(Screens.Preferences.screen)
                "Logout" -> {
                    authViewModel.signOut()
                    navigateController.navigate(Screens.Landing.screen) {
                        // Clear the back stack so user can't go back after logging out
                        popUpTo(0) { inclusive = true }
                    }
                }
                "Redeem" -> navigateController.navigate(Screens.RedeemGifts.screen)
                "Bookmark" -> navigateController.navigate(Screens.DocumentBookmarkedList.screen)
                "Upload" -> navigateController.navigate(Screens.DocumentUploadedList.screen)
                "Download" -> navigateController.navigate(Screens.DocumentDownloadedList.screen)
                "Preview" -> navigateController.navigate(Screens.ProfilePreview.screen)
            }
        },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .width(45.dp)
                    .height(45.dp),
                contentDescription = "shortcut_item",
                painter = painterResource(id = ShortCutCardItem.icon),
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = ShortCutCardItem.title,
                fontSize = 10.sp,
            )
        }
    }
}
