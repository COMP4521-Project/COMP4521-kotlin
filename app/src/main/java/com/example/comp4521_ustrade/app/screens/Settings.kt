
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.screens.Screens
import com.example.comp4521_ustrade.auth.AuthViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    onNavigateBack: () -> Unit = {},
    navigationController: NavController,
    authViewModel: AuthViewModel
) {

    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.Settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.Back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if(isDarkModeEnabled) USTBlue_dark else USTBlue,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Account Section
            item { Text(stringResource(id = R.string.Account), fontWeight = FontWeight.Bold) }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = USTBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.Person,
                            text = stringResource(R.string.EditProfile),
                            onClick = { navigationController.navigate(Screens.EditProfile.screen) }
                        )
                        SettingsItem(
                            icon = Icons.Default.Lock,
                            text = stringResource(R.string.ChangePassword),
                            onClick = { navigationController.navigate(Screens.EditPassword.screen) }
                        )
                        SettingsItem(
                            icon = Icons.Default.Notifications,
                            text = stringResource(R.string.Notifications),
                            onClick = { navigationController.navigate(Screens.NotificationSettings.screen) }
                        )
                    }
                }
            }

            // Resources Section
            item { 
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.Resources), fontWeight = FontWeight.Bold) 
            }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = USTBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.CardGiftcard,
                            text = stringResource(R.string.RedeemGifts),
                            onClick = { navigationController.navigate(Screens.RedeemGifts.screen) }
                        )
                        SettingsItem(
                            icon = Icons.Default.Bookmark,
                            text = stringResource(R.string.Bookmarks),
                            onClick = { navigationController.navigate(Screens.DocumentBookmarkedList.screen) }
                        )
                        SettingsItem(
                            icon = Icons.Default.Upload,
                            text = stringResource(R.string.UploadedFiles),
                            onClick = { navigationController.navigate(Screens.DocumentUploadedList.screen) }
                        )
                    }
                }
            }

            // Support & About Section
            item { 
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.SupportAbout), fontWeight = FontWeight.Bold) 
            }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = USTBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.Settings,
                            text = stringResource(R.string.Preferences),
                            onClick = { navigationController.navigate(Screens.Preferences.screen) }
                        )
                        SettingsItem(
                            icon = Icons.Default.Info,
                            text = stringResource(R.string.AboutApp),
                            onClick = { navigationController.navigate(Screens.AboutApp.screen) }
                        )
                        SettingsItem(
                            icon = Icons.Default.ExitToApp,
                            text = stringResource(R.string.LogOut),
                            onClick = {
                                authViewModel.signOut()
                                navigationController.navigate(Screens.Landing.screen) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(text = text)
        }
    }
}