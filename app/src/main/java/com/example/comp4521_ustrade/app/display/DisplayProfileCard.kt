package com.example.comp4521_ustrade.app.display

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.ProfileCard
import com.example.comp4521_ustrade.app.models.ProfileCardData
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel

@Composable
fun displayProfileCard(modifier: Modifier = Modifier, userViewModel: UserViewModel) {
    val username = userViewModel.username.observeAsState().value
    val uploadCount = userViewModel.uploadCount.observeAsState().value ?: 0
    val user = userViewModel.user.observeAsState().value

    val userData = ProfileCardData(
        name = username ?: "Loading...",
        profilePicture = R.drawable.user1,
        upload_count = uploadCount,
        download_count = 60
    )

    ProfileCard(ProfileCardData = userData, userViewModel = userViewModel)
}