package com.example.comp4521_ustrade.app.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.comp4521_ustrade.app.models.CourseCardItem
import com.example.comp4521_ustrade.app.screens.Screens
import com.example.comp4521_ustrade.ui.theme.USTBlue

@Composable
fun CourseCard(CourseCardItem: CourseCardItem,  navigateController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current.applicationContext
    Card(
        modifier = Modifier
            .fillMaxWidth().height(210.dp)
            .clickable { Toast.makeText(context, "${CourseCardItem.courseTitle} pressed", Toast.LENGTH_SHORT).show() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        // TODO: navigate to course details screen
        onClick = {
//            navigateController.navigate(Screens.DocumentDetails.screen)
            navigateController.navigate(Screens.DocumentDetails.screen + "/${CourseCardItem.documentId}")
        }
    ) {
        Column() {
            AsyncImage(
                model = CourseCardItem.thumbnailUrl,
                contentDescription = "Course thumbnail",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                contentScale = ContentScale.FillBounds,
//                error = painterResource(id = R.drawable.comp1), // Fallback image
//                placeholder = painterResource(id = R.drawable.comp1) // Loading placeholder
            )


            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .background(USTBlue)
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                    text = CourseCardItem.course + " - " + CourseCardItem.courseTitle,
                    color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                    maxLines = 1,

                    )

                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp),
                    text = CourseCardItem.courseYear + " " + CourseCardItem.courseSemester,
                    color = Color.White, fontWeight = FontWeight.Light, fontSize = 10.sp,
                    maxLines = 1,

                    )
            }
        }

    }
}
