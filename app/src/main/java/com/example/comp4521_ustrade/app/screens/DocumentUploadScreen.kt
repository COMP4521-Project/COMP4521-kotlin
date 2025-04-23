package com.example.comp4521_ustrade.app.screens

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.app.components.CustomTextField
import com.example.comp4521_ustrade.app.components.DropdownList
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentUploadScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    navViewModel: NavViewModel
) {
    var subject by remember { mutableStateOf("") }
    var subjectCode by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var professor by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isFileUploaded by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf<String?>(null) }

    // Add states for dropdown expansions
    var isSubjectExpanded by remember { mutableStateOf(false) }
    var isSubjectCodeExpanded by remember { mutableStateOf(false) }
    var isYearExpanded by remember { mutableStateOf(false) }
    var isSemesterExpanded by remember { mutableStateOf(false) }

    // Add dropdown options
    val subjects = listOf("Science", "Engineering", "Business")
    val subjectCodes = listOf(
        "COMP", "CPEG", "CSIT", "IEDA", "MECH", "CIVL", "ELEC"
    )
    val years = listOf("2024", "2023", "2022", "2021")
    val semesters = listOf("Fall", "Spring", "Summer")

    // Calculate dynamic spacing based on dropdown state
    val subjectSpacing = if (isSubjectExpanded) (subjects.size * 48).dp else 8.dp
    val subjectCodeSpacing = if (isSubjectCodeExpanded) (subjectCodes.size * 48).dp else 8.dp
    val yearSpacing = if (isYearExpanded) (years.size * 48).dp else 8.dp
    val semesterSpacing = if (isSemesterExpanded) (semesters.size * 48).dp else 8.dp

    // Check if all required fields are filled and file is uploaded
    val isFormValid = remember(subject, subjectCode, year, semester, title, isFileUploaded) {
        subject.isNotBlank() &&
        subjectCode.isNotBlank() &&
        year.isNotBlank() &&
        semester.isNotBlank() &&
        title.isNotBlank() &&
        isFileUploaded
    }

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
                title = { Text("Upload Document") },
                navigationIcon = {
                    IconButton(onClick = {
                        navViewModel.setSelectedScreen(Screens.Home)
                        onNavigateBack()
                    }
                    ) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Upload box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .border(
                            width = 1.dp,
                            color = if (isFileUploaded) MaterialTheme.colorScheme.primary 
                                   else Color.Gray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { 
                            /* Handle upload */ 
                            // After successful file upload:
                            // isFileUploaded = true
                            // fileName = "uploaded_file.pdf"
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (isFileUploaded && fileName != null) {
                            Text(
                                text = fileName!!,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Click to change file",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Upload",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Click to Upload or Take a photo",
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "(Max File size: 25 MB)",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            item {
                Column {
                    DropdownList(
                        title = "Subject",
                        selectedItem = subject.ifEmpty { "Select subject" },
                        onItemSelected = { 
                            subject = it
                            isSubjectExpanded = false
                        },
                        expanded = isSubjectExpanded,
                        onExpandedChange = { isSubjectExpanded = it },
                        content = subjects
                    )
                    Spacer(modifier = Modifier.height(subjectSpacing))
                }
            }

            item {
                Column {
                    DropdownList(
                        title = "Subject Code",
                        selectedItem = subjectCode.ifEmpty { "Select subject code" },
                        onItemSelected = { 
                            subjectCode = it
                            isSubjectCodeExpanded = false
                        },
                        expanded = isSubjectCodeExpanded,
                        onExpandedChange = { isSubjectCodeExpanded = it },
                        content = subjectCodes
                    )
                    Spacer(modifier = Modifier.height(subjectCodeSpacing))
                }
            }

            item {
                Column {
                    DropdownList(
                        title = "Year",
                        selectedItem = year.ifEmpty { "Select year" },
                        onItemSelected = { 
                            year = it
                            isYearExpanded = false
                        },
                        expanded = isYearExpanded,
                        onExpandedChange = { isYearExpanded = it },
                        content = years
                    )
                    Spacer(modifier = Modifier.height(yearSpacing))
                }
            }

            item {
                Column {
                    DropdownList(
                        title = "Semester",
                        selectedItem = semester.ifEmpty { "Select semester" },
                        onItemSelected = { 
                            semester = it
                            isSemesterExpanded = false
                        },
                        expanded = isSemesterExpanded,
                        onExpandedChange = { isSemesterExpanded = it },
                        content = semesters
                    )
                    Spacer(modifier = Modifier.height(semesterSpacing))
                }
            }

            item {
                CustomTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Title",
                    placeholder = "Enter document title",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                CustomTextField(
                    value = professor,
                    onValueChange = { professor = it },
                    label = "Professor (optional)",
                    placeholder = "Enter professor name",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                CustomTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description (optional)",
                    placeholder = "Enter document description",
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Button(
                    onClick = { /* Handle upload */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid
                ) {
                    Text("Upload")
                }
            }

            // If form is not valid and user has attempted to submit, show error message
            item {
                if (!isFormValid) {
                    val errorMessage = buildString {
                        if (!isFileUploaded) append("• Please upload a file\n")
                        if (subject.isBlank()) append("• Subject is required\n")
                        if (subjectCode.isBlank()) append("• Subject code is required\n")
                        if (year.isBlank()) append("• Year is required\n")
                        if (semester.isBlank()) append("• Semester is required\n")
                        if (title.isBlank()) append("• Title is required\n")
                    }
                    
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
} 