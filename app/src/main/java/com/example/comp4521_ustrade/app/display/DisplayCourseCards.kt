package com.example.comp4521_ustrade.app.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.CourseCard
import com.example.comp4521_ustrade.app.models.CourseCardItem

@Composable
fun DisplayCourseCards(modifier: Modifier = Modifier) {
    val courseList = listOf(

        CourseCardItem(R.drawable.comp1, "COMP4521 Lecture7 Lecture note", "2025", "Spring"),
        CourseCardItem(R.drawable.comp2, "Course 2", "2025", "Semester 2"),
        CourseCardItem(R.drawable.comp3, "Course 3", "2025", "Semester 3"),
        CourseCardItem(R.drawable.comp4, "Course 4", "2025", "Semester 3"),
        CourseCardItem(R.drawable.comp5, "Course 5", "2025", "Semester 3"),
        CourseCardItem(R.drawable.comp6, "Course 6", "2025", "Semester 3"),
        CourseCardItem(R.drawable.elec1, "Course 4", "2025", "Semester 4"),
        CourseCardItem(R.drawable.elec2, "Course 5", "2025", "Semester 5"),

        )



    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(courseList.size) { index ->
            CourseCard(CourseCardItem = courseList[index])
        }
    }
}