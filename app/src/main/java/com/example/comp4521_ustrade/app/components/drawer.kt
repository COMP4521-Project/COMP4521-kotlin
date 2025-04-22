package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.app.models.DrawerItem
import kotlinx.coroutines.launch

//side navigation drawer
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState
) {
    val items = listOf(
        DrawerItem(Icons.Filled.Home, "COMP", "COMP"),
        DrawerItem(Icons.Filled.Person, "PHYS", "PHYS"),
        DrawerItem(Icons.Filled.Face, "ELEC", "ELEC")
    )
    val selectedItem = remember { mutableStateOf(items[0]) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Select by Subjects")
    }
    items.forEach { item ->
        NavigationDrawerItem(
            label = { Text(item.label) },
            icon = { Icon(item.icon, contentDescription = null) },
            badge = {
                if (item.secondary_label.isNotEmpty()) {
                    Text(item.secondary_label)
                }
            },
            selected = item == selectedItem.value,
            onClick = {
                selectedItem.value = item
                // Navigate to the subject screen with the subject as a parameter
                navController.navigate("subject/${item.secondary_label}")
                // Close the drawer
                scope.launch {
                    drawerState.close()
                }
            }
        )
    }
}