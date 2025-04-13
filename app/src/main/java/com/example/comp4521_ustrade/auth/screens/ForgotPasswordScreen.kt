package com.example.comp4521_ustrade.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onResetPassword: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var countdown by remember { mutableStateOf(0) }
    var hasError by remember { mutableStateOf(false) }
    
    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000)
            countdown--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        Text("E-mail", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        
        TextField(
            value = email,
            onValueChange = { 
                email = it
                hasError = false 
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter your email") },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            isError = hasError,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
            )
        )

        if (hasError) {
            Text(
                text = "Please enter your email",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { 
                if (email.isNotBlank()) {
                    countdown = 60
                    onResetPassword(email)
                } else {
                    hasError = true
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (countdown > 0) 
                    Color(0xFF213F6C).copy(alpha = 0.5f) 
                else 
                    Color(0xFF213F6C)
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = countdown == 0
        ) {
            Text(
                text = if (countdown > 0) 
                    "Wait ${countdown}s to resend" 
                else 
                    "Send Reset Link",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Back to Login")
        }
    }
}
