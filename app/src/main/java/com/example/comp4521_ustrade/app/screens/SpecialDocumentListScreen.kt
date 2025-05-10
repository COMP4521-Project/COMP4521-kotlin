package com.example.comp4521_ustrade.app.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.DocumentCard
import com.example.comp4521_ustrade.app.data.dao.Document
import com.example.comp4521_ustrade.app.data.dao.User
import com.example.comp4521_ustrade.app.data.repository.DocumentRepository
import com.example.comp4521_ustrade.app.data.repository.UserRepository
import com.example.comp4521_ustrade.app.models.CourseCardItem
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialDocumentListScreen(
    pageTitle: String,
    onNavigateBack: () -> Unit,
    onDocumentClick: (String) -> Unit,
    navigationController: NavController,
    modifier: Modifier = Modifier,
    navViewModel: NavViewModel,
    userViewModel: UserViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    var documentRepository = remember { DocumentRepository() }

    var likedDocuments by remember { mutableStateOf<List<Document>>(emptyList()) }


    val userid = userViewModel.userid.observeAsState().value
    val userRepository = remember { UserRepository() }
    var user by remember { mutableStateOf<User?>(null) }

    // Get the localized page title for display
    val localizedPageTitle = when (pageTitle) {
        "Favorites" -> stringResource(R.string.Favorites)
        "Bookmarked" -> stringResource(R.string.Bookmarked)
        "Uploaded" -> stringResource(R.string.Uploaded)
        else -> pageTitle
    }

    // Get the document type based on the page title
    val documentType = when (pageTitle) {
        stringResource(R.string.Favorites), "Favorites" -> "Favorites"
        stringResource(R.string.Bookmarked), "Bookmarked" -> "Bookmarked"
        stringResource(R.string.Uploaded), "Uploaded" -> "Uploaded"
        else -> pageTitle
    }


    LaunchedEffect(userid, pageTitle) {
        println("DEBUG: LaunchedEffect triggered with userId: $userid and pageTitle: $pageTitle")
        if (userid != null) {
            try {
                user = userRepository.getUser(userid)
                println("DEBUG: User fetched successfully: ${user?.uid}")
            } catch (e: Exception) {
                println("DEBUG: Failed to fetch user: ${e.message}")
            }
        }
    }

    LaunchedEffect(user, userid, pageTitle) {
        println("DEBUG: Processing documents for pageTitle: $pageTitle")
        if (user == null) {
            println("DEBUG: User is null, can't fetch documents")
            return@LaunchedEffect
        }

        when (documentType) {
            "Favorites" -> {
                val likedList = user?.documents?.liked ?: emptyList()
                println("DEBUG: Found ${likedList.size} liked document IDs")
                likedDocuments = documentRepository.getDocumentsByIds(likedList)
                println("DEBUG: Retrieved ${likedDocuments.size} liked documents")
            }
            "Bookmarked" -> {
                val bookmarkedList = user?.documents?.bookmarked ?: emptyList()
                println("DEBUG: Found ${bookmarkedList.size} bookmarked document IDs")
                likedDocuments = documentRepository.getDocumentsByIds(bookmarkedList)
            }
            "Uploaded" -> {
                val uploadedList = user?.documents?.uploaded ?: emptyList()
                println("DEBUG: Found ${uploadedList.size} uploaded document IDs")
                likedDocuments = documentRepository.getDocumentsByIds(uploadedList)
            }
        }
    }



    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = pageTitle) },
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
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Search bar and filter button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { },
                    active = false,
                    onActiveChange = { },
                    modifier = Modifier
                        .weight(1f),

                    placeholder = { Text(text= stringResource(R.string.FilesFound, likedDocuments.size))}

                ) { }
                
                IconButton(
                    onClick = { /* TODO: Implement filter action */ },
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }
            
            Text(
                text = stringResource(R.string.FilesFound, likedDocuments.size),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            if (likedDocuments.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No liked documents",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(likedDocuments) { document ->
                        DocumentCard(
                            document = document,
                            onClick = {
                                navigationController.navigate(Screens.DocumentDetails.screen + "/${document.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

