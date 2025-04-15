package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit
) {
    Text(label, style = MaterialTheme.typography.bodyMedium)

    Spacer(modifier = Modifier.height(2.dp))

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = placeholder?.let { { Text(it) } },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        isError = isError,
        visualTransformation = if (passwordVisible) 
            VisualTransformation.None 
        else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityChange) {
                Icon(
                    imageVector = if (passwordVisible)
                        Icons.Outlined.VisibilityOff
                    else Icons.Outlined.Visibility,
                    contentDescription = if (passwordVisible)
                        "Hide password"
                    else "Show password"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = if (isError) 
                MaterialTheme.colorScheme.error 
            else MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = if (isError) 
                MaterialTheme.colorScheme.error 
            else MaterialTheme.colorScheme.outline
        )
    )
}