package com.example.comp4521_ustrade.app.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.data.dao.UserNotification
import com.example.comp4521_ustrade.app.data.repository.NotificationRepository
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notification(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    navViewModel: NavViewModel,
    userViewModel: UserViewModel,
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All", "Read", "Unread")
    
    val userId = userViewModel.userid.observeAsState().value
    val notificationRepository = remember { NotificationRepository() }
    var notifications by remember { mutableStateOf<List<UserNotification>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    
    // Load notifications when the screen is displayed
    LaunchedEffect(userId) {
        userId?.let {
            notifications = notificationRepository.getUserNotifications(it)
        }
    }
    
    val unreadCount = notifications.count { !it.read }

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
                title = { Text("Notifications") },
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
                                    text = title,
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

            // Notification List with animation
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(),
                modifier = Modifier.weight(1f)
            ) {
                val filteredNotifications = when (selectedTab) {
                    0 -> notifications // All
                    1 -> notifications.filter { it.read } // Read
                    2 -> notifications.filter { !it.read } // Unread
                    else -> emptyList()
                }
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(filteredNotifications) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = {
                                // Mark as read when clicked
                                if (!notification.read) {
                                    coroutineScope.launch {
                                        notificationRepository.markNotificationAsRead(userId!!, notification.id)
                                        // Refresh notifications
                                        notifications = notificationRepository.getUserNotifications(userId)
                                    }
                                }
                                
                                // Navigate to related document if available
                                notification.related_document_id?.let { docId ->
                                    navController.navigate(Screens.DocumentDetails.screen + "/$docId")
                                }
                            }
                        )
                        Divider()
                    }

                    item {
                        if (filteredNotifications.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No notifications",
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
fun NotificationItem(notification: UserNotification, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Avatar with better error handling
        AsyncImage(
            model = notification.from_user_propic,
            contentDescription = "User avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            error = painterResource(R.drawable.user1),  // Default icon for errors
            fallback = painterResource(R.drawable.user1),  // Default icon when null
            placeholder = painterResource(R.drawable.user1)  // Default icon during loading
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = notification.from_user_name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Format timestamp to readable time
        val formattedTime = remember(notification.updated_at) {
            formatTimestamp(notification.updated_at)
        }
        
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp)
        )
        
        // Unread indicator
        if (!notification.read) {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(8.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
        }
    }
}

// Helper function to format timestamp to readable time
private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60 * 1000 -> "Just now"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}m ago"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}h ago"
        diff < 7 * 24 * 60 * 60 * 1000 -> "${diff / (24 * 60 * 60 * 1000)}d ago"
        else -> {
            val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}