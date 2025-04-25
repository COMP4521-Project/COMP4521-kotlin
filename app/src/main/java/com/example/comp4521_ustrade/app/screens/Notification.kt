package com.example.comp4521_ustrade.app.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.helper.mapValueToString
import com.example.comp4521_ustrade.app.models.NotificationData
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notification(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    navViewModel: NavViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All", "Read", "Unread")
    val unreadCount = getNotifications(0).count { !it.isRead }  // Count unread notifications


    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.Notifications)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navViewModel.setSelectedScreen(Screens.Home)
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if(isDarkModeEnabled) USTBlue_dark else USTBlue,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = { Divider(thickness = 2.dp) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = { 
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = mapValueToString(title),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                                if (title == "Unread" && unreadCount > 0) {  // Show badge only for Unread tab
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .size(16.dp)
                                            .background(Color.Red, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = unreadCount.toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Add animation for content
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(),
                modifier = Modifier.weight(1f)
            ) {
                // Notification List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(getNotifications(selectedTab)) { notification ->
                        NotificationItem(notification)
                        Divider()
                    }

                    item {
                        if (getNotifications(selectedTab).isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.NoNotifications),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Avatar
        Image(
            painter = painterResource(R.drawable.user1),
            contentDescription = "User avatar",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
        )
        // AsyncImage(
        //     model = notification.userAvatar,
        //     contentDescription = "User avatar",
        //     modifier = Modifier
        //         .size(40.dp)
        //         .clip(CircleShape),
        //     contentScale = ContentScale.Crop
        // )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = notification.userName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Text(
            text = notification.time,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

fun getNotifications(tabIndex: Int): List<NotificationData> {
    // This is a sample implementation - you should replace this with actual data from your backend
    val allNotifications = listOf(
        NotificationData(
            userAvatar = "https://example.com/avatar1.jpg",
            userName = "Hannah Flores",
            message = "Liked your upload.",
            time = "10:04 AM",
            isRead = true
        ),
        NotificationData(
            userAvatar = "https://example.com/avatar1.jpg",
            userName = "Hannah Flores",
            message = "has sent you a message",
            time = "10:04 AM",
            isRead = false
        )
    )

    return when (tabIndex) {
        0 -> allNotifications // All
        1 -> allNotifications.filter { it.isRead } // Read
        2 -> allNotifications.filter { !it.isRead } // Unread
        else -> emptyList()
    }
}