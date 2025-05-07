import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.DisplayFields
import com.example.comp4521_ustrade.app.models.DisplayFieldItem
import com.example.comp4521_ustrade.ui.theme.USTBlue

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
                title = { Text(stringResource(R.string.AboutApp)) },
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
            Text(
                text = stringResource(R.string.About),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = stringResource(R.string.LearnMore),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}