import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.DropdownList
import com.example.comp4521_ustrade.app.components.ToggleButton
import com.example.comp4521_ustrade.app.viewmodel.AppSettingsViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    onNavigateBack: () -> Unit = {},
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    appSettingsViewModel: AppSettingsViewModel
) {
    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var selectedLanguage by remember {
        mutableStateOf(sharedPreferences.getString("selected_language", "English") ?: "English")
    }

    val isChatbotEnabled by appSettingsViewModel.isChatbotEnabled.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("English", "Traditional Chinese", "Simplified Chinese")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.Preference)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.Back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = (if (isDarkTheme) USTBlue_dark else USTBlue),
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = USTBlue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column {
                    ToggleButton(
                        text = stringResource(R.string.LightMode),
                        checked = isDarkTheme,
                        onCheckedChange = {
                            onThemeChange(it)
                            sharedPreferences.edit().putBoolean("is_dark_theme", it).apply()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = USTBlue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column {
                    ToggleButton(
                        text = stringResource(R.string.Chatbot),
                        checked = isChatbotEnabled,
                        onCheckedChange = { appSettingsViewModel.setChatbotEnabled(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    DropdownList(
                        title = stringResource(R.string.Language),
                        selectedItem = selectedLanguage,
                        onItemSelected = {
                            selectedLanguage = it
                            sharedPreferences.edit().putString("selected_language", it).apply()
                            when (it) {
                                "English" -> updateLocale(context, Locale.ENGLISH)
                                "Traditional Chinese" -> updateLocale(context, Locale("zh", "TW"))
                                "Simplified Chinese" -> updateLocale(context, Locale("zh", "CN"))
                            }
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        content = languages
                    )
                }
            }
        }
    }
}

private fun updateLocale(context: Context, locale: Locale) {
    val resources = context.resources
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)

    // Update the resources configuration
    resources.updateConfiguration(configuration, resources.displayMetrics)

    // Recreate the activity
    if (context is Activity) {
        context.recreate()
    }
}