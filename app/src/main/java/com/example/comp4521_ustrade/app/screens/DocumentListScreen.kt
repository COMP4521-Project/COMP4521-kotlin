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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
    var filteredDocumentList by remember { mutableStateOf<List<Document>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }
    
    // Filter states
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf<String?>(null) }
    var selectedSemester by remember { mutableStateOf<String?>(null) }
    var selectedSort by remember { mutableStateOf<String?>(null) }
    var expandedYearMenu by remember { mutableStateOf(false) }
    var expandedSemesterMenu by remember { mutableStateOf(false) }
    var expandedSortMenu by remember { mutableStateOf(false) }
    
    // Get unique years and semesters from documents
    val years = remember(documentList) {
        documentList.map { it.year }.distinct().sorted()
    }
    val semesters = remember(documentList) {
        documentList.map { it.semester }.distinct().sorted()
    }

    LaunchedEffect(Unit) {
        documentList = documentRepository.getSubjectSpecificDocuments(pageTitle)
        filteredDocumentList = documentList
    }

    // Apply filters and sorting whenever criteria or document list changes
    LaunchedEffect(selectedYear, selectedSemester, selectedSort, documentList) {
        filteredDocumentList = documentList
            .filter { document ->
                (selectedYear == null || document.year == selectedYear) &&
                (selectedSemester == null || document.semester == selectedSemester)
            }
            .let { filtered ->
                when (selectedSort) {
                    "Most Liked" -> filtered.sortedByDescending { it.like_count }
                    "Least Liked" -> filtered.sortedBy { it.like_count }
                    "Newest First" -> filtered.sortedByDescending { it.upload_date }
                    "Oldest First" -> filtered.sortedBy { it.upload_date }
                    else -> filtered
                }
            }
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
                    placeholder = { Text(text = "Search in ${pageTitle}") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                )

                IconButton(
                    onClick = { showFilterDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }

            // Show active filters if any
            if (selectedYear != null || selectedSemester != null || selectedSort != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Active filters:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (selectedYear != null) {
                        Text(
                            text = "Year: $selectedYear",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (selectedSemester != null) {
                        Text(
                            text = "Semester: $selectedSemester",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (selectedSort != null) {
                        Text(
                            text = "Sort: $selectedSort",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.FilesFound, filteredDocumentList.size),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            if (filteredDocumentList.isEmpty()) {
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
                    filteredDocumentList.isNotEmpty() -> {
                        LazyColumn {
                            items(filteredDocumentList) { document ->
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

    // Filter Dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Documents") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Year Filter
                    ExposedDropdownMenuBox(
                        expanded = expandedYearMenu,
                        onExpandedChange = { expandedYearMenu = it }
                    ) {
                        TextField(
                            value = selectedYear ?: "Select Year",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYearMenu) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedYearMenu,
                            onDismissRequest = { expandedYearMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Years") },
                                onClick = {
                                    selectedYear = null
                                    expandedYearMenu = false
                                }
                            )
                            years.forEach { year ->
                                DropdownMenuItem(
                                    text = { Text(year) },
                                    onClick = {
                                        selectedYear = year
                                        expandedYearMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Semester Filter
                    ExposedDropdownMenuBox(
                        expanded = expandedSemesterMenu,
                        onExpandedChange = { expandedSemesterMenu = it }
                    ) {
                        TextField(
                            value = selectedSemester ?: "Select Semester",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSemesterMenu) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSemesterMenu,
                            onDismissRequest = { expandedSemesterMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Semesters") },
                                onClick = {
                                    selectedSemester = null
                                    expandedSemesterMenu = false
                                }
                            )
                            semesters.forEach { semester ->
                                DropdownMenuItem(
                                    text = { Text(semester) },
                                    onClick = {
                                        selectedSemester = semester
                                        expandedSemesterMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Sort Filter
                    ExposedDropdownMenuBox(
                        expanded = expandedSortMenu,
                        onExpandedChange = { expandedSortMenu = it }
                    ) {
                        TextField(
                            value = selectedSort ?: "Sort by",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSortMenu) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSortMenu,
                            onDismissRequest = { expandedSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Default") },
                                onClick = {
                                    selectedSort = null
                                    expandedSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Most Liked") },
                                onClick = {
                                    selectedSort = "Most Liked"
                                    expandedSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Least Liked") },
                                onClick = {
                                    selectedSort = "Least Liked"
                                    expandedSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Most Recent Upload") },
                                onClick = {
                                    selectedSort = "Most Recent Upload"
                                    expandedSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Least Recent Upload") },
                                onClick = {
                                    selectedSort = "Least Recent Upload"
                                    expandedSortMenu = false
                                }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFilterDialog = false }
                ) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedYear = null
                        selectedSemester = null
                        selectedSort = null
                        showFilterDialog = false
                    }
                ) {
                    Text("Clear All")
                }
            }
        )
    }
}