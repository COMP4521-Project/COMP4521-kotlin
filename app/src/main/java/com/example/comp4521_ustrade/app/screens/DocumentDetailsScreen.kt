package com.example.comp4521_ustrade.app.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.DisplayOnlyFields
import com.example.comp4521_ustrade.app.components.DocumentPreviewSlider
import com.example.comp4521_ustrade.app.data.dao.Document
import com.example.comp4521_ustrade.app.data.dao.User
import com.example.comp4521_ustrade.app.data.repository.DocumentRepository
import com.example.comp4521_ustrade.app.data.repository.UserRepository
import com.example.comp4521_ustrade.app.data.repository.NotificationRepository
import com.example.comp4521_ustrade.app.data.dao.UserNotification
import com.example.comp4521_ustrade.app.models.CourseCardItem
import com.example.comp4521_ustrade.app.models.DisplayOnlyFieldItem
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import com.example.comp4521_ustrade.ui.theme.USTWhite
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DocumentDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    documentId: String,
    userViewModel: UserViewModel,
    navController: NavController
) {
    val userid = userViewModel.userid.observeAsState().value
    val documentRepository = remember { DocumentRepository() }
    var document by remember { mutableStateOf<Document?>(null) }
    var uploader by remember { mutableStateOf<User?>(null) }

    val userRepository = remember { UserRepository() }
    var user by remember { mutableStateOf<User?>(null) }

    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val notificationRepository = remember { NotificationRepository() }

    LaunchedEffect(documentId) {
        document = documentRepository.getDocument(documentId)
        uploader = document?.let { userRepository.getUser(it.uploaded_by) }
        user = userid?.let { userRepository.getUser(userid) }
    }
    LaunchedEffect(user, documentId) {
        val likedList = user?.documents?.liked ?: emptyList()
        val dislikedList = user?.documents?.disliked ?: emptyList()
        val bookmarkedList = user?.documents?.bookmarked ?: emptyList()
        isLiked = likedList.any { it.trim() == documentId.trim() }
        isDisliked = dislikedList.any { it.trim() == documentId.trim() }
        isBookmarked = bookmarkedList.any { it.trim() == documentId.trim() }
    }


    val documentFields = listOf(
        DisplayOnlyFieldItem(title = stringResource(R.string.Year), value= (document?.year ?: "") + " " + (document?.semester ?: "")),
        document?.let { DisplayOnlyFieldItem(title=stringResource(R.string.Course), value= it.course) },
        document?.let { DisplayOnlyFieldItem(title = stringResource(R.string.UploadDate), value = it.upload_date) },
        document?.let { DisplayOnlyFieldItem(title = stringResource(R.string.Description), value = it.description) }
    )



    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    // Sample preview images (replace with actual document preview images)
    val previewImages = listOf(
        R.drawable.comp1,  // Replace with your actual drawable resources
        R.drawable.comp2,
        R.drawable.comp3
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.DocumentDetails)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.Back))
                    }
                },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if(isDarkModeEnabled) USTBlue_dark else USTBlue,
            titleContentColor = USTWhite,
            navigationIconContentColor = USTWhite),
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                val bookmarkedList = user?.documents?.bookmarked ?: emptyList()
                                if (!bookmarkedList.contains(documentId)) {
                                    if (userid != null) {
                                        userRepository.addBookmarkedDocumentToUser(
                                            userid,
                                            documentId
                                        )
                                    }
                                    isBookmarked = true
                                } else {
                                    if (userid != null) {
                                        userRepository.removeBookmarkedDocumentFromUser(
                                            userid,
                                            documentId
                                        )
                                    }
                                    isBookmarked = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = stringResource(R.string.Bookmark),
                            tint = if (isBookmarked) Color.Black else USTWhite
                        )
                    }
                    IconButton(onClick = { /* Handle share */ }) {
                        Icon(Icons.Default.Share, stringResource(R.string.Share), tint = USTWhite)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Replace the existing preview Surface with:
            DocumentPreviewSlider(
                images = previewImages,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            // Document title
            document?.let {
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // User info row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user1),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                
                Spacer(modifier = Modifier.width(12.dp))

                if (uploader != null) {
                    Text(
                        text = uploader!!.first_name + " " + uploader!!.last_name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable { navController.navigate(Screens.OthersProfile.screen + "/${uploader!!.uid}") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Document details
            DisplayOnlyFields(
                fields = documentFields,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Replace the existing download button section with:
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row (verticalAlignment = Alignment.CenterVertically ){
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                val likedList = user?.documents?.liked ?: emptyList()
                                val dislikedList = user?.documents?.disliked ?: emptyList()
                                
                                if (!likedList.contains(documentId)) {
                                    // Add like
                                    documentRepository.addLikeToDocument(documentId)
                                    if (userid != null) userRepository.addLikedDocumentToUser(userid, documentId)
                                    
                                    // Send notification to document uploader
                                    document?.let { doc ->
                                        if (userid != null && user != null && doc.uploaded_by != userid) {
                                            // Create and send notification only if the liker is not the uploader
                                            val notification = UserNotification(
                                                id = UUID.randomUUID().toString(),
                                                from_user_id = userid,
                                                from_user_name = "${user!!.first_name} ${user!!.last_name}",
                                                from_user_propic = user!!.profile_pic?.toString(),
                                                updated_at = System.currentTimeMillis(),
                                                message = "${user!!.first_name} liked your document: ${doc.title}",
                                                read = false,
                                                related_document_id = documentId
                                            )
                                            
                                            // Add notification to uploader's notifications
                                            notificationRepository.addNotification(doc.uploaded_by, notification)
                                        }
                                    }
                                    
                                    // If previously disliked, remove dislike
                                    if (dislikedList.contains(documentId)) {
                                        documentRepository.removeDislikeFromDocument(documentId)
                                        if (userid != null) userRepository.removeDislikedDocumentFromUser(userid, documentId)
                                    }
                                } else {
                                    // Remove like
                                    documentRepository.removeLikeFromDocument(documentId)
                                    if (userid != null) userRepository.removeLikedDocumentFromUser(userid, documentId)
                                }
                                
                                // Refresh user and document state
                                user = userid?.let { userRepository.getUser(it) }
                                document = documentRepository.getDocument(documentId)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = " ",
                            tint = if (isLiked) Color.Red else Color.Gray
                        )
                    }
                    Text(
                        modifier = Modifier.offset((-4).dp),
                        text = (document?.like_count ?: 0).toString(),
                        color = Color.Gray
                    )
                }

                Row (verticalAlignment = Alignment.CenterVertically ){
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                val likedList = user?.documents?.liked ?: emptyList()
                                val dislikedList = user?.documents?.disliked ?: emptyList()
                                if (!dislikedList.contains(documentId)) {
                                    documentRepository.addDislikeToDocument(documentId)
                                    if (userid != null) userRepository.addDislikedDocumentToUser(userid, documentId)
                                    // If previously liked, remove like
                                    if (likedList.contains(documentId)) {
                                        documentRepository.removeLikeFromDocument(documentId)
                                        if (userid != null) userRepository.removeLikedDocumentFromUser(userid, documentId)
                                    }
                                } else {
                                    documentRepository.removeDislikeFromDocument(documentId)
                                    if (userid != null) userRepository.removeDislikedDocumentFromUser(userid, documentId)
                                }
                                // Refresh user and document state
                                user = userid?.let { userRepository.getUser(it) }
                                document = documentRepository.getDocument(documentId)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                            contentDescription = "",
                            tint = if (isDisliked) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                    Text(modifier = Modifier.offset((-4).dp),
                        text = (document?.dislike_count ?: 0).toString(),
                        color = Color.Gray)
                }

                Button(
                    onClick = { /* Handle download */ },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = stringResource(R.string.Download),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.DownloadDocument))
                }
            }
        }
    }
}