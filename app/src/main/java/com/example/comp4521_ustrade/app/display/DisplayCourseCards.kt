package com.example.comp4521_ustrade.app.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.CourseCard
import com.example.comp4521_ustrade.app.data.dao.Document
import com.example.comp4521_ustrade.app.data.repository.DocumentRepository
import com.example.comp4521_ustrade.app.models.CourseCardItem

@Composable
fun DisplayCourseCards(modifier: Modifier = Modifier, navigateController: NavController, subject: String="", userID: String="") {
    // Sample data for course cards

//    val courseList = listOf(
//        CourseCardItem(R.drawable.unavailable, "COMP4521 Lecture7 Lecture note", "2025", "Spring"),
//        CourseCardItem(R.drawable.comp2, "Course 2", "2025", "Semester 2"),
//        CourseCardItem(R.drawable.comp3, "Course 3", "2025", "Semester 3"),
//        CourseCardItem(R.drawable.comp4, "Course 4", "2025", "Semester 3"),
//        CourseCardItem(R.drawable.comp5, "Course 5", "2025", "Semester 3"),
//        CourseCardItem(R.drawable.comp6, "Course 6", "2025", "Semester 3"),
//        CourseCardItem(R.drawable.elec1, "Course 4", "2025", "Semester 4"),
//        CourseCardItem(R.drawable.elec2, "Course 5", "2025", "Semester 5"),
//        )

    val documentRepository = DocumentRepository()
    var documentList by remember { mutableStateOf<List<Document>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

//    LaunchedEffect(Unit) {
//        documentList = documentRepository.getSubjectSpecificDocuments(subject)
//    }

    LaunchedEffect(subject, userID) {
        isLoading = true
        documentList = when {
            userID != "" && subject == "" -> documentRepository.getUserSpecificDocuments(userID)
            subject != "" && userID == "" -> documentRepository.getSubjectSpecificDocuments(subject)
            else -> emptyList() // or documentRepository.getAllDocuments()
        }
        isLoading = false
    }

    // Map Document to CourseCardItem
    val courseList: List<CourseCardItem> = documentList.map { document ->
        CourseCardItem(
            thumbnail = R.drawable.unavailable,
            course = document.course,
            courseTitle = document.title,
            courseYear = document.year,
            courseSemester = document.semester,
            documentId = document.id,
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            courseList.isEmpty() -> {
                Text(
                    text = "No document in this subject uploaded yet",
                    color = Color.Gray
                )
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(courseList.size) { index ->
                        CourseCard(
                            CourseCardItem = courseList[index],
                            navigateController = navigateController,
                        )
                    }
                }
            }
        }
    }
}