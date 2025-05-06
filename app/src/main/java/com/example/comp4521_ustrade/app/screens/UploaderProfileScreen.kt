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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.comp4521_ustrade.app.data.dao.Document
import com.example.comp4521_ustrade.app.data.dao.User
import com.example.comp4521_ustrade.app.data.repository.DocumentRepository
import com.example.comp4521_ustrade.app.data.repository.UserRepository
import com.example.comp4521_ustrade.app.display.DisplayCourseCards
import com.example.comp4521_ustrade.app.display.DisplayUploaderProfileCard
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import com.example.comp4521_ustrade.ui.theme.USTWhite

@Composable
fun UploaderProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    navigationController: NavController,
    userViewModel: UserViewModel,
    uploaderId : String,
) {

    val context = LocalContext.current

    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    val userRepository = remember { UserRepository() }
    val documentRepository = remember { DocumentRepository() }
    var otherUser by remember { mutableStateOf<User?>(null) }
    var uploadedDocuments by remember { mutableStateOf<List<Document>>(emptyList()) }

    // Fetch the other user's info and their uploaded documents
    LaunchedEffect(uploaderId) {
        otherUser = userRepository.getUser(uploaderId)
        val uploadedIds = otherUser?.documents?.uploaded ?: emptyList()
        uploadedDocuments = documentRepository.getDocumentsByIds(uploadedIds)
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
                        text = "Uploader Profile",
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
                DisplayUploaderProfileCard(uploaderId = uploaderId)
            }
            
            // Add these sections below the profile card
            Spacer(modifier = Modifier.height(16.dp))
            AboutSection(uploaderId = uploaderId)
            Spacer(modifier = Modifier.height(16.dp))
            UploadsSection(navigationController, uploaderId = uploaderId)
        }
    }
}

@Composable
private fun AboutSection(uploaderId: String) {
    val userRepository = remember { UserRepository() }
    var uploader by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(uploaderId) {
        uploader = userRepository.getUser(uploaderId)
    }


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
        uploader?.description?.let {
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
private fun UploadsSection(navigateController: NavController, uploaderId:String) {
    val userRepository = remember { UserRepository() }
    var uploader by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(uploaderId) {
        uploader = userRepository.getUser(uploaderId)
    }


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
            navigateController = navigateController,
            userID = uploaderId
        )
    }
} 