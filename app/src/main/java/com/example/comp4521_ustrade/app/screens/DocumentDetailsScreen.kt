package com.example.comp4521_ustrade.app.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import android.app.DownloadManager
import android.os.Environment
import android.widget.Toast
import java.io.File

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

    // Add download function
    fun downloadFile(context: Context, fileUrl: String, fileName: String) {
        try {
            val request = DownloadManager.Request(android.net.Uri.parse(fileUrl))
                .setTitle(fileName)
                .setDescription("Downloading document")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            
            // Update download count
            coroutineScope.launch {
                document?.let { doc ->
                    documentRepository.updateDocument(doc.id, mapOf("downloadCount" to (doc.downloadCount + 1)))
                    document = documentRepository.getDocument(doc.id)
                }
            }
            
            Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

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
            document?.let { doc ->
                // Check if document has upload_documents with cover images
                if (doc.upload_documents.isNotEmpty()) {
                    // Create a list of all available cover images
                    val coverImages = doc.upload_documents
                        .mapNotNull { it.coverImageUrl }
                        .filter { it.isNotEmpty() }
                    
                    if (coverImages.isNotEmpty()) {
                        // Create a state to track current page
                        val pagerState = rememberPagerState(initialPage = 0) { coverImages.size }
                        val currentPage = pagerState.currentPage
                        
                        // Show image carousel/pager for multiple images
                        Column {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) { index ->
                                AsyncImage(
                                    model = coverImages[index],
                                    contentDescription = "Document Cover ${index + 1}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    error = painterResource(id = getDefaultPreviewImage(doc.course.toString())),
                                    placeholder = painterResource(id = getDefaultPreviewImage(doc.course.toString()))
                                )
                            }
                            
                            // Page indicator dots
                            if (coverImages.size > 1) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    repeat(coverImages.size) { index ->
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = 2.dp)
                                                .size(8.dp)
                                                .background(
                                                    color = if (index == currentPage) 
                                                        MaterialTheme.colorScheme.primary 
                                                    else 
                                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // No cover images, show default for course
                        Image(
                            painter = painterResource(id = getDefaultPreviewImage(doc.course.toString())),
                            contentDescription = "Document Preview",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    // No upload documents, show default for course
                    Image(
                        painter = painterResource(id = getDefaultPreviewImage(doc.course.toString())),
                        contentDescription = "Document Preview",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

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
                                    // Document is not liked yet - add like
                                    
                                    // 1. Increment document's like count in Firestore
                                    document?.let { doc ->
                                        // Calculate new like count
                                        val newLikeCount = doc.like_count + 1
                                        // Update the document with new like count
                                        documentRepository.updateDocument(doc.id, mapOf("like_count" to newLikeCount))
                                    }
                                    
                                    // 2. Add document ID to user's liked documents list
                                    if (userid != null) {
                                        // Make sure to handle the case where liked might be null
                                        val updatedLikedList = likedList.toMutableList()
                                        updatedLikedList.add(documentId)
                                        
                                        // Update the user's liked documents in Firestore
                                        userRepository.updateUserField(userid, "documents.liked", updatedLikedList)
                                        
                                        // Also update local user state to reflect the change
                                        isLiked = true
                                    }
                                    
                                    // 3. Send notification to document uploader
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
                                    
                                    // 4. If previously disliked, remove dislike
                                    if (dislikedList.contains(documentId)) {
                                        document?.let { doc ->
                                            // Decrement dislike count
                                            val newDislikeCount = Math.max(0, doc.dislike_count - 1)
                                            documentRepository.updateDocument(doc.id, mapOf("dislike_count" to newDislikeCount))
                                        }
                                        
                                        // Remove from user's disliked list
                                        if (userid != null) {
                                            val updatedDislikedList = dislikedList.toMutableList()
                                            updatedDislikedList.remove(documentId)
                                            userRepository.updateUserField(userid, "documents.disliked", updatedDislikedList)
                                            
                                            // Update local state
                                            isDisliked = false
                                        }
                                    }
                                    
                                    // 5. Refresh document and user data to update UI
                                    document = documentRepository.getDocument(documentId)
                                    user = userid?.let { userRepository.getUser(it) }
                                } else {
                                    // Already liked - remove like
                                    
                                    // 1. Decrement document's like count
                                    document?.let { doc ->
                                        // Ensure like count doesn't go below 0
                                        val newLikeCount = Math.max(0, doc.like_count - 1)
                                        documentRepository.updateDocument(doc.id, mapOf("like_count" to newLikeCount))
                                    }
                                    
                                    // 2. Remove document ID from user's liked list
                                    if (userid != null) {
                                        val updatedLikedList = likedList.toMutableList()
                                        updatedLikedList.remove(documentId)
                                        userRepository.updateUserField(userid, "documents.liked", updatedLikedList)
                                        
                                        // Update local state
                                        isLiked = false
                                    }
                                    
                                    // 3. Refresh document and user data
                                    document = documentRepository.getDocument(documentId)
                                    user = userid?.let { userRepository.getUser(it) }
                                }
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
                    onClick = { 
                        document?.upload_documents?.forEach { uploadDoc ->
                            downloadFile(context, uploadDoc.file_url, uploadDoc.document_name)
                        }
                    },
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

// Add this function to get default course images
private fun getDefaultPreviewImage(courseName: String): Int {
    return when (courseName) {
        "COMP4521" -> R.drawable.comp1
        "COMP2011" -> R.drawable.comp2
        "COMP2012" -> R.drawable.comp3
        else -> R.drawable.comp1 // Default fallback
    }
}