package com.example.comp4521_ustrade

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.comp4521_ustrade.ui.theme.COMP4521ustradeTheme
import com.example.comp4521_ustrade.auth.navigation.AuthNavigation
import com.example.comp4521_ustrade.app.screens.HomePage
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set default dark theme preference to false
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        if (!sharedPreferences.contains("is_dark_theme")) {
            sharedPreferences.edit().putBoolean("is_dark_theme", false).apply()
        }

        // Apply saved language preference
        val savedLanguage = sharedPreferences.getString("selected_language", "English") ?: "English"
        val locale = when (savedLanguage) {
            "English" -> Locale.ENGLISH
            "Traditional Chinese" -> Locale("zh", "TW")
            "Simplified Chinese" -> Locale("zh", "CN")
            else -> Locale.ENGLISH
        }
        updateLocale(this, locale)

        setContent {
            var isDarkTheme by remember {
                mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
            }

            COMP4521ustradeTheme(darkTheme = isDarkTheme)  {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavigation(
                        onAuthSuccess = {
                            HomePage(
                                isDarkTheme = isDarkTheme,
                                onThemeChange = { newValue ->
                                    isDarkTheme = newValue },
                            )
                        }
                    )
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
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    COMP4521ustradeTheme {
        Greeting("Android")
    }
}

@Composable
fun HomeScreen() {
    Greeting("Authenticated User")
}