import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.app.components.DisplayFieldItem
import com.example.comp4521_ustrade.app.components.DisplayFields

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen(
    onNavigateBack: () -> Unit = {}
) {
    val fields = listOf(
        DisplayFieldItem(title = "Version", value = "1.0.0"),
        DisplayFieldItem(title = "Terms of Service", value = "https://www.google.com", onClick = {
            // Handle click on Terms of Service
        }),
        DisplayFieldItem(title = "Privacy Policy", value = "https://www.google.com", onClick = {
            // Handle click on Privacy Policy
        })
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About this app") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        DisplayFields(
            fields = fields,
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
        )
    }
}