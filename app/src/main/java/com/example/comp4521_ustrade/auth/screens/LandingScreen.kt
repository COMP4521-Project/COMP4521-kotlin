package com.example.comp4521_ustrade.auth.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Spacer(modifier = Modifier.weight(2f))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "USTrade Logo",
            modifier = Modifier.size(512.dp)
        )

        
        Spacer(modifier = Modifier.weight(1f))
        
        // Buttons
        OutlinedButton(
            onClick = onNavigateToRegister,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text="Register",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF213F6C)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text="Login",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(64.dp))
    }
} 