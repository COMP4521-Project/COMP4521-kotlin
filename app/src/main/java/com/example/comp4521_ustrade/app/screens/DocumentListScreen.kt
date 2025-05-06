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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.comp4521_ustrade.app.data.repository.DocumentRepository
import com.example.comp4521_ustrade.app.models.CourseCardItem
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentListScreen(
    pageTitle: String,
    onNavigateBack: () -> Unit,
    onDocumentClick: (String) -> Unit,
    navigationController: NavController,
    modifier: Modifier = Modifier,
    navViewModel: NavViewModel
) {
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }
    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }
    val documentRepository = remember { DocumentRepository() }
    var documentList by remember { mutableStateOf<List<Document>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(Unit) {
        documentList = documentRepository.getSubjectSpecificDocuments(pageTitle)
    }

    Scaffold(
        topBar = {
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
                    containerColor = if (isDarkModeEnabled) USTBlue_dark else USTBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search bar and filter button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Custom Search Bar Implementation
                TextField(
                    value = query,
                    onValueChange = { newQuery ->
                        query = newQuery
                        searchJob?.cancel()
                        if (query.isBlank()) {
                            isLoading = true
                            searchJob = coroutineScope.launch {
                                documentList = documentRepository.getSubjectSpecificDocuments(pageTitle)
                                isLoading = false
                            }
                        } else {
                            isLoading = true
                            searchJob = coroutineScope.launch {
                                documentList = documentRepository.searchDocumentsBySubjectAndCourse(pageTitle, query)
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(text = "Search documents in ${pageTitle}") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                )

                IconButton(
                    onClick = { /* TODO: Implement filter action */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }

            Text(
                text = stringResource(R.string.FilesFound, documentList.size),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            if (documentList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "no_documents_found",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                when {
                    isLoading -> {
                        Text(
                            text = "loading",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    documentList.isNotEmpty() -> {
                        LazyColumn {
                            items(documentList) { document ->
                                DocumentCard(
                                    document = document,
                                    onClick = {
                                        navigationController.navigate(Screens.DocumentDetails.screen + "/${document.id}")
                                    }
                                )
                            }
                        }
                    }
                    query.isNotBlank() -> {
                        Text(
                            text = "no_results_found",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}