
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.CustomTextField
import com.example.comp4521_ustrade.app.data.repository.StorageRepository
import com.example.comp4521_ustrade.app.data.repository.UserRepository
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit = {},
    userViewModel: UserViewModel
) {
    val userId = userViewModel.userid.observeAsState().value
    var username = userViewModel.username.observeAsState().value
    val profilePicUrl = userViewModel.user.observeAsState().value?.profile_pic

    val firstNameLiveData = userViewModel.firstName.observeAsState()
    var firstName by remember { mutableStateOf(firstNameLiveData.value ?: "") }

    val lastNameLiveData = userViewModel.lastName.observeAsState()
    var lastName by remember { mutableStateOf(lastNameLiveData.value ?: "") }

    val dateOfBirthLiveData = userViewModel.dateOfBirth.observeAsState()
    var dateOfBirth by remember { mutableStateOf(dateOfBirthLiveData.value ?: "") }

    val descriptionLiveData = userViewModel.description.observeAsState()
    var description by remember { mutableStateOf(descriptionLiveData.value ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0.0) }
    
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input
    )

    val context = LocalContext.current
    val storageRepository = remember { StorageRepository() }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (userId != null) {
                isUploading = true
                uploadProgress = 0.0
                
                userViewModel.viewModelScope.launch {
                    try {
                        val imageUrl = storageRepository.uploadProfileImage(
                            imageUri = it,
                            userId = userId,
                            onProgress = { progress ->
                                uploadProgress = progress
                            }
                        )
                        
                        // Update user profile with new image URL
                        val userRepository = UserRepository()
                        userRepository.updateUser(userId, mapOf("profile_pic" to imageUrl))
                        userViewModel.refreshUserData()
                        isUploading = false
                    } catch (e: Exception) {
                        e.printStackTrace()
                        isUploading = false
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.EditProfile)) },
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
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        progress = uploadProgress.toFloat(),
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Center)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { imagePickerLauncher.launch("image/*") }
                    ) {
                        if (profilePicUrl != null) {
                            AsyncImage(
                                model = profilePicUrl,
                                contentDescription = stringResource(R.string.ProfilePicture),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                error = painterResource(id = R.drawable.default_profile_pic)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.default_profile_pic),
                                contentDescription = stringResource(R.string.DefaultProfilePicture),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change profile picture",
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .size(32.dp)
                                .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                                .padding(4.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (username != null) {
                CustomTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = stringResource(R.string.FirstName),
                    placeholder = stringResource(R.string.EnterYourFirstname)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = stringResource(R.string.LastName),
                placeholder = stringResource(R.string.EnterYourLastname)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            CustomTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = stringResource(R.string.DateOfBirth),
                placeholder = stringResource(R.string.SelectYourDateOfBirth),
                onClick = { showDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange,"Select date")
                    }
                }
            )
            
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val instant = java.time.Instant.ofEpochMilli(millis)
                                val date = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                dateOfBirth = date.format(formatter)
                            }
                            showDatePicker = false
                        }) {
                            Text(stringResource(R.string.OK))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text(stringResource(R.string.Cancel))
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            CustomTextField(
                value = description,
                onValueChange = { description = it },
                label = stringResource(R.string.Description),
                singleLine = true,
                minLines = 1,
                placeholder = stringResource(R.string.EnterYourDescription)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    val userProfileUpdates = hashMapOf(
                        "first_name" to firstName,
                        "last_name" to lastName,
                        "date_of_birth" to dateOfBirth,
                        "description" to description
                    )

                    val userRepository = UserRepository()

                    if (userId != null) {
                        userViewModel.viewModelScope.launch {
                            try {
                                userRepository.updateUser(userId, userProfileUpdates)
                                userViewModel.refreshUserData()
                                onNavigateBack()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.SaveChanges))
            }
        }
    }
} 