package com.example.comp4521_ustrade.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Home(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(Color.Cyan)) {
        Text("Home Page")
    }
}