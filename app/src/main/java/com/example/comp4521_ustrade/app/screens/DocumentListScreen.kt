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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.DocumentCard
import com.example.comp4521_ustrade.app.models.Document
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentListScreen(
    pageTitle: String,
    onNavigateBack: () -> Unit,
    onDocumentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    navViewModel: NavViewModel
) {
    var searchQuery by remember { mutableStateOf("") }


    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    // Sample data - replace with actual data from your backend
    val documents = List(5) { index ->
        Document(
            id = "doc$index",
            title = "COMP4521",
            subtitle = "Mobile App development",
            term = "Spring, 2025",
            type = "Lecture 7 Lecture note",
            imageUrl = ""
        )
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
                    placeholder = { Text(text= stringResource(R.string.FilesFound, documents.size))}
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
                text = stringResource(R.string.FilesFound, documents.size),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(documents) { document ->
                    DocumentCard(
                        document = document,
                        onClick = { onDocumentClick(document.id) }
                    )
                }
            }
        }
    }
}

