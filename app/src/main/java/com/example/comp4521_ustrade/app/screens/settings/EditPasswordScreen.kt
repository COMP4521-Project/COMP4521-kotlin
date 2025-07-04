import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.CustomPasswordTextField
import com.example.comp4521_ustrade.auth.AuthViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordScreen(
    onNavigateBack: () -> Unit = {},
    authViewModel: AuthViewModel
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.EditPassword)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.Back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = USTBlue,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            CustomPasswordTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = stringResource(R.string.ExistingPassword),
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.EnterYourExistingPassword),
                isError = false,
                passwordVisible = currentPasswordVisible,
                onPasswordVisibilityChange = { currentPasswordVisible = !currentPasswordVisible }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            CustomPasswordTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = stringResource(R.string.NewPassword),
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(R.string.EnterYourNewPassword),
                isError = false,
                passwordVisible = newPasswordVisible,
                onPasswordVisibilityChange = { newPasswordVisible = !newPasswordVisible }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            val successText = stringResource(R.string.PasswordUpdatedSuccessfully)
            val failureText = stringResource(R.string.FailedToUpdatePassword)
            Button(
                onClick = {
                    errorMessage = null
                    successMessage = null
                    authViewModel.changePassword(currentPassword, newPassword) { success, error ->
                        if (success) {
                            successMessage = successText
                            // Optionally navigate back or clear fields
                        } else {
                            errorMessage = error ?: failureText
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.SaveChanges))
            }

            errorMessage?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
            successMessage?.let {
                Text(it, color = Color.Green, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}