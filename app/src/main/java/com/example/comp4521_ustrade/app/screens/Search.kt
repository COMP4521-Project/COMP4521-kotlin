package com.example.comp4521_ustrade.app.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.data.dao.Document
import com.example.comp4521_ustrade.app.data.repository.DocumentRepository
import com.example.comp4521_ustrade.app.models.CourseCardItem
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import com.example.comp4521_ustrade.ui.theme.USTgray
import kotlinx.coroutines.launch

fun getRecentOpenedDocuments(context: Context): List<String> {
    val prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return prefs.getStringSet("recent_opened_documents", LinkedHashSet())?.toList() ?: emptyList()
}

fun saveRecentOpenedDocuments(context: Context, docIds: List<String>) {
    val prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    prefs.edit().putStringSet("recent_opened_documents", LinkedHashSet(docIds)).apply()
}

fun clearRecentOpenedDocuments(context: Context) {
    val prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    prefs.edit().remove("recent_opened_documents").apply()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    modifier: Modifier = Modifier,
    navigationController: androidx.navigation.NavController,
    ) {
    val context = LocalContext.current

    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(true) }
    var recentOpenedDocIds by remember { mutableStateOf(getRecentOpenedDocuments(context)) }
    var recentOpenedDocs by remember { mutableStateOf<List<Document>>(emptyList()) }

//    var searchHistory = listOf("COMP", "Kotlin", "Compose", "Java")

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    val documentRepository = remember { DocumentRepository() }
    var results by remember { mutableStateOf<List<Document>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(active) {
        if (active) {
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(recentOpenedDocIds) {
        if (recentOpenedDocIds.isNotEmpty()) {
            recentOpenedDocs = documentRepository.getDocumentsByIds(recentOpenedDocIds)
//            // Optionally, sort to match the order of IDs
//            recentOpenedDocs = recentOpenedDocIds.mapNotNull { id -> recentOpenedDocs.find { it.id == id } }
        } else {
            recentOpenedDocs = emptyList()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.dp)
                .background(if (isDarkModeEnabled) USTBlue_dark else USTBlue)
        ) {

        }
        SearchBar(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 0.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { if (it.isFocused) active = true },
            query = query,
            onQueryChange = { newQuery ->
                query = newQuery
                if (query.isBlank()) {
                    results = emptyList()
                    isLoading = false
                } else {
                    isLoading = true
                    coroutineScope.launch {
                        results = documentRepository.searchDocumentsByCourseContains(query)
                        isLoading = false
                    }
                }
            },
            onSearch = {
                isLoading = true
                coroutineScope.launch {
                    results = documentRepository.searchDocumentsByCourseContains(query)
                    isLoading = false
                }
                Toast.makeText(context, "Search for $query", Toast.LENGTH_SHORT).show()
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = {
                Text(text = stringResource(R.string.SearchResources))
            },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            colors = SearchBarDefaults.colors(
                containerColor = colorScheme.background
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if (active) {
                    IconButton(onClick = {
                        query = ""
                        active = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear"
                        )
                    }
                }
            },

            ) {
            when {
                isLoading -> {
                    Text(text = stringResource(R.string.Loading))
                }

                query.isEmpty() -> {
                    Column (modifier = Modifier.padding(horizontal = 8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text= stringResource(R.string.NoRecentlyOpenedDocuments),
                                modifier = Modifier.weight(1f),
                                fontSize = 18.sp
                            )
                            TextButton(
                                onClick = {
                                    clearRecentOpenedDocuments(context)
                                    recentOpenedDocIds = emptyList()
                                    recentOpenedDocs = emptyList()
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(text = stringResource(R.string.Clear), fontSize = 14.sp)
                            }
                        }
                        if (recentOpenedDocs.isEmpty()) {
                            Text(text= stringResource(R.string.NoRecentlyOpenedDocuments))
                        } else {
                            recentOpenedDocs.forEach { doc ->
                                ListItem(
                                    headlineContent = { Text("${doc.course} ${doc.title} (${doc.year} ${doc.semester})") },
                                    leadingContent = {
                                        Icon(Icons.Default.History, contentDescription = "Recent")
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // Update recent opened docs (move to top)
                                            val newRecent = listOf(doc.id) + recentOpenedDocIds.filter { it != doc.id }
                                            val limitedRecent = newRecent.take(10)
                                            recentOpenedDocIds = limitedRecent
                                            saveRecentOpenedDocuments(context, limitedRecent)

                                            // Navigate to document details
                                            navigationController.navigate(Screens.DocumentDetails.screen + "/${doc.id}")
                                        }
                                )
                            }
                        }
                    }
                }

                results.isNotEmpty() -> {
                    // Show search results
                    Column {
                        results.forEach { document ->
                            ListItem(
                                headlineContent = { Text("${document.course} ${document.title} (${document.year} ${document.semester})") },
                                leadingContent = {
                                    Icon(Icons.Default.Search, contentDescription = "Result")
                                },
                                trailingContent = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = "Likes",
                                            tint = Color.Red
                                        )
                                        Text(
                                            text = document.like_count.toString(),
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val newRecent = listOf(document.id) + recentOpenedDocIds.filter { it != document.id }
                                        val limitedRecent = newRecent.take(10)
                                        recentOpenedDocIds = limitedRecent
                                        saveRecentOpenedDocuments(context, limitedRecent)
                                        // Handle document click
                                        navigationController.navigate(Screens.DocumentDetails.screen + "/${document.id}")
                                    }
                            )
                        }
                    }
                }

                else -> {
                    Text("No results found")
                }
            }
        }
    }
}

