package com.example.comp4521_ustrade.app.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.app.components.DisplayFieldItem
import com.example.comp4521_ustrade.app.components.DisplayFields
import com.example.comp4521_ustrade.app.components.FunctionCardData
import com.example.comp4521_ustrade.app.components.FunctionCardsGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fields = listOf(
        DisplayFieldItem(title = "Powered by", value = "Botpress"),
        DisplayFieldItem(title = "Version", value = "1.0.0")
    )
    val functionCards = listOf(
        FunctionCardData(
            icon = Icons.Outlined.Navigation,
            title = "App Navigation Support"
        ),
        FunctionCardData(
            icon = Icons.Outlined.Book,
            title = "Resource Recommendations"
        ),
        FunctionCardData(
            icon = Icons.Outlined.QuestionAnswer,
            title = "FAQ and Troubleshooting"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
            // AI Bot Icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "AI Bot",
                    modifier = Modifier.size(120.dp)
                )
            }

            // Version section
            DisplayFields(
                fields = fields,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                defaultFontSize = 16,
                defaultTextStyle = MaterialTheme.typography.bodyLarge
            )

            // Functions section
            Text(
                text = "Functions",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Function cards grid
            FunctionCardsGrid(functionCards = functionCards)
        }
    }
}