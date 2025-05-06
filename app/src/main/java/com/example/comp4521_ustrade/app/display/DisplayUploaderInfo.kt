package com.example.comp4521_ustrade.app.display

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.ProfileCard
import com.example.comp4521_ustrade.app.components.UploaderProfileCard
import com.example.comp4521_ustrade.app.data.dao.User
import com.example.comp4521_ustrade.app.data.repository.UserRepository
import com.example.comp4521_ustrade.app.models.ProfileCardData

@Composable
fun DisplayUploaderProfileCard(modifier: Modifier = Modifier, uploaderId:String) {

    val userRepository = remember { UserRepository() }
    var uploader by remember { mutableStateOf<User?>(null) }

    // Fetch uploader info from Firestore
    LaunchedEffect(uploaderId) {
        uploader = userRepository.getUser(uploaderId)
    }


    val userData = ProfileCardData(
        name = if (uploader != null) "${uploader!!.first_name} ${uploader!!.last_name}" else "Loading...",
        profilePicture = R.drawable.user1,
        upload_count = uploader?.upload_count ?: 0,
        download_count = 0
    )

    UploaderProfileCard(ProfileCardData = userData)
}