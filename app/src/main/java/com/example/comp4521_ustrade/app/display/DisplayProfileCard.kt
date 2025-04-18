package com.example.comp4521_ustrade.app.display

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.ProfileCard
import com.example.comp4521_ustrade.app.models.ProfileCardData
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel

@Composable
fun displayProfileCard(modifier: Modifier = Modifier, userViewModel : UserViewModel) {
    val userData = ProfileCardData(
        name = "Hachi ware",
        profilePicture = R.drawable.user1,
        upload_count = 10,
        download_count = 60
    )

    ProfileCard(ProfileCardData = userData, userViewModel = userViewModel)
}