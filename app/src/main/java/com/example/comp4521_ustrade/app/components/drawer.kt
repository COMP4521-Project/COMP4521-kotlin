package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.models.DrawerItem
import com.example.comp4521_ustrade.app.viewmodel.CourseViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_light
import com.example.comp4521_ustrade.ui.theme.USTWhite
import com.example.comp4521_ustrade.ui.theme.USTgray
import kotlinx.coroutines.launch

//side navigation drawer
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState,
    courseViewModel: CourseViewModel
) {
    val items = courseViewModel.courseTitles
    val selectedItem = remember { mutableStateOf(items[0]) }
    val scope = rememberCoroutineScope()

    val customColors = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = USTBlue, // Purple for selected background
        unselectedContainerColor = Color.Transparent, // Transparent for unselected background
//        selectedIconColor = Color.White, // White for selected icon
//        unselectedIconColor = Color.Gray, // Gray for unselected icon
        selectedTextColor = USTgray, // White for selected text
        unselectedTextColor = USTWhite, // Gray for unselected text
        selectedBadgeColor = Color.Red, // Red for selected badge
        unselectedBadgeColor = Color.Transparent // Transparent for unselected badge
    )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.SelectBySubjects), color = USTWhite)
    }
    items.forEach { item ->
        NavigationDrawerItem(
            label = { Text(item) },
            shape = RoundedCornerShape(4.dp),
            selected = item == selectedItem.value,
            onClick = {
                selectedItem.value = item
                // Navigate to the subject screen with the subject as a parameter
                navController.navigate("subject/${item}")
                // Close the drawer
                scope.launch {
                    drawerState.close()
                }
            },
            colors = customColors
        )
    }
}