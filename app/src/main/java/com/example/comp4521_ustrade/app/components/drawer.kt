package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.app.models.DrawerItem

//side navigation drawer
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(modifier: Modifier = Modifier) {
    val items = listOf(
        DrawerItem(Icons.Filled.Home, "COMP", ""),
        DrawerItem(Icons.Filled.Person, "PHYS", ""),
        DrawerItem(Icons.Filled.Face, "ELEC", "")
    )
    val selectedItem = remember { mutableStateOf(items[0]) }

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
            }
        )
    }
}