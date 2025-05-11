package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.models.ProfileCardData
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel

@Composable
fun ProfileCard(modifier: Modifier = Modifier, ProfileCardData: ProfileCardData, userViewModel: UserViewModel) {
    val uploadCountString = userViewModel.upload_count.observeAsState().value
    val uploadCount = uploadCountString?.toIntOrNull() ?: 0
    val profilePicUrl = userViewModel.user.observeAsState().value?.profile_pic

    var level = 0;

    if (uploadCount in 5..11) {
        level = 1
    } else if (uploadCount in 12..19) {
        level = 2
    } else if (uploadCount >= 20) {
        level = 3
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.surface,
                contentColor = colorScheme.onSurface
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        ) {
            Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = modifier
                        .weight(2f)
                        .size(100.dp)
                        .padding(start = 8.dp, end = 16.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                ) {
                    if (profilePicUrl != null) {
                        AsyncImage(
                            model = profilePicUrl,
                            contentDescription = stringResource(R.string.ProfilePicture),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.default_profile_pic)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.default_profile_pic),
                            contentDescription = stringResource(R.string.DefaultProfilePicture),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Column(
                    modifier = modifier.weight(3f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = ProfileCardData.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier.padding(2.dp))
                    ContributorTag(level = level)
                }
            }

            Row(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$uploadCount", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = stringResource(R.string.Upload),
                        fontSize = 16.sp,
                        modifier = modifier.padding(top = 8.dp)
                    )
                }


            }
        }
    }
}

@Composable
fun UploaderProfileCard(modifier: Modifier = Modifier, ProfileCardData: ProfileCardData, profilePicUrl: String? = null) {
    val uploadCount = ProfileCardData.upload_count

    var level = 0;

    if (uploadCount in 5..11) {
        level = 1
    } else if (uploadCount in 12..19) {
        level = 2
    } else if (uploadCount >= 20) {
        level = 3
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = colorScheme.surface,
                contentColor = colorScheme.onSurface
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        ) {
            Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = modifier
                        .weight(2f)
                        .size(100.dp)
                        .padding(start = 8.dp, end = 16.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                ) {
                    if (profilePicUrl != null) {
                        AsyncImage(
                            model = profilePicUrl,
                            contentDescription = stringResource(R.string.ProfilePicture),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.user1)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.user1),
                            contentDescription = stringResource(R.string.DefaultProfilePicture),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Column(
                    modifier = modifier.weight(3f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = ProfileCardData.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier.padding(2.dp))
                    ContributorTag(level = level)
                }
            }

            Row(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$uploadCount", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = stringResource(R.string.Upload),
                        fontSize = 16.sp,
                        modifier = modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
