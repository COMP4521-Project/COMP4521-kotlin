package com.example.comp4521_ustrade

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
        var isDarkTheme by remember { mutableStateOf(false) }

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
                                onThemeChange = { isDarkTheme = it },
                            )

                        }
                    )


                }
            }
        }
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