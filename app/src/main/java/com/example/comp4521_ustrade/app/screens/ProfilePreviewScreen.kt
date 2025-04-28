import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.comp4521_ustrade.app.display.DisplayCourseCards
import com.example.comp4521_ustrade.app.display.displayProfileCard
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import com.example.comp4521_ustrade.ui.theme.USTWhite

@Composable
fun ProfilePreviewScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    navigationController: NavController,
    userViewModel: UserViewModel
) {

    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(if(isDarkModeEnabled) USTBlue_dark else USTBlue)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Row() {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "Profile Preview",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = USTWhite
                    )
                }
            }
        }
        Column {
            Box(
                modifier = Modifier
                    .width(420.dp).height(280.dp)
                    .padding(start = 16.dp, top = 80.dp, end = 16.dp),
            ) {
                displayProfileCard(userViewModel = userViewModel)
            }
            
            // Add these sections below the profile card
            Spacer(modifier = Modifier.height(16.dp))
            AboutSection(userViewModel = userViewModel)
            Spacer(modifier = Modifier.height(16.dp))
            UploadsSection(navigationController)
        }
    }
}

@Composable
private fun AboutSection(userViewModel : UserViewModel) {
    val descriptionLiveData = userViewModel.description.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "About",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        descriptionLiveData.value?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
private fun UploadsSection(navigateController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Uploads",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DisplayCourseCards(
            modifier = Modifier.fillMaxWidth(),
            navigateController = navigateController
        )
    }
} 