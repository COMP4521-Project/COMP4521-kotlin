package com.example.comp4521_ustrade.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun CustomTextField(
    titleFontSize: Int = 16,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    passwordVisible: Boolean = false
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
          //  .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(2.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth().then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
            placeholder = placeholder?.let { { Text(it) } },
            shape = RoundedCornerShape(8.dp),
            singleLine = singleLine,
            minLines = minLines,
            isError = isError,
            trailingIcon = trailingIcon,
            visualTransformation = if (passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
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
}