package com.example.comp4521_ustrade.app.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comp4521_ustrade.ui.theme.USTgray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(modifier: Modifier = Modifier) {
    val context = LocalContext.current.applicationContext

    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(true) }

    var searchHistory = listOf("COMP", "Kotlin", "Compose","Java")

    val focusRequester = remember { FocusRequester() }

    //handle keyboard focus
    val focusManager = LocalFocusManager.current


    LaunchedEffect(active) {
        if (active) {
            focusRequester.requestFocus() // Request focus when active
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus() // Clear focus to hide the keyboard
                })
            },
        contentAlignment = Alignment.TopCenter

    ) {
        SearchBar(
            modifier = modifier
                .focusRequester(focusRequester) // Attach the focus requester
                .onFocusChanged { if (it.isFocused) active = true },

            query = query,
            onQueryChange = { query = it },
            onSearch = {
                //trigger a toast here
                Toast.makeText(context, "Search for $query", Toast.LENGTH_SHORT).show()

            },
            active = active,
            onActiveChange = { active = it },
            placeholder = {
                Text(text = "Search Resources")
            },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            colors = SearchBarDefaults.colors(containerColor = if (active) Color.White else USTgray),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if (active) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable {
                                query = ""
                                active = false
                            }
                        )

                    }
                }
            },
        ) {
            Text(
                text = "Recent Searches",
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 18.sp,
            )
            searchHistory.takeLast(3).forEach { item ->
                ListItem(
                    headlineContent = { Text(text = item) },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                        )
                    },
                    colors = ListItemDefaults.colors(
                        headlineColor = Color.Black,
                        leadingIconColor = Color.Black,
                        containerColor = Color.Transparent,
                    ),

                    modifier = Modifier.clickable {
                        query = item
                    }
                )

            }
        }
    }


}