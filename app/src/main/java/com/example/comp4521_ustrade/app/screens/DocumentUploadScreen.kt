package com.example.comp4521_ustrade.app.screens

import DocumentUploadViewModel
import UploadState
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.CustomTextField
import com.example.comp4521_ustrade.app.components.DropdownList
import com.example.comp4521_ustrade.app.models.Course
import com.example.comp4521_ustrade.app.screens.camera.CameraView
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.app.viewmodel.UserViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Converts a Compose ImageBitmap to Android's Bitmap
 */
fun ImageBitmap.asBitmap(): Bitmap {
    val width = this.width
    val height = this.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val buffer = IntArray(width * height)
    
    // Read pixels from ImageBitmap
    this.readPixels(buffer)
    
    // Write pixels to Android Bitmap
    bitmap.setPixels(buffer, 0, width, 0, 0, width, height)
    
    return bitmap
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DocumentUploadScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    navViewModel: NavViewModel,
    userViewModel: UserViewModel,
    docUploadViewModel: DocumentUploadViewModel,
    onUploadComplete: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uploadState by docUploadViewModel.uploadState.collectAsState()

    var subject by remember { mutableStateOf("") }
    var subjectCode by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var professor by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isFileUploaded by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf<String?>(null) }
    var capturedPhotos by remember { mutableStateOf<List<ImageBitmap>>(emptyList()) }
    var imageLoadingError by remember { mutableStateOf<String?>(null) }
    var showUploadOptions by remember { mutableStateOf(false) }
    var selectedFiles by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var isUploading by remember { mutableStateOf(false) }
    var currentUploadStatus by remember { mutableStateOf("") }

    //user-related
    val userId = userViewModel.userid.observeAsState().value

    // Add states for dropdown expansions
    var isYearExpanded by remember { mutableStateOf(false) }
    var isSemesterExpanded by remember { mutableStateOf(false) }

    // Add dropdown options
    val years = listOf("2025","2024", "2023", "2022", "2021")
    val semesters = listOf("Fall", "Spring", "Summer", "Winter")

    // Calculate dynamic spacing based on dropdown state
    val yearSpacing = if (isYearExpanded) (years.size * 48).dp else 8.dp
    val semesterSpacing = if (isSemesterExpanded) (semesters.size * 48).dp else 8.dp

    // Check if all required fields are filled and file is uploaded
    val isFormValid = remember(subject, subjectCode, year, semester, title, isFileUploaded) {
        subject.isNotBlank() &&
                subjectCode.isNotBlank() &&
                year.isNotBlank() &&
                semester.isNotBlank() &&
                title.isNotBlank()
//               && isFileUploaded
    }


    val sharedPreferences = remember {
        context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    var isDarkModeEnabled by remember {
        mutableStateOf(sharedPreferences.getBoolean("is_dark_theme", false))
    }

    var showCamera by remember { mutableStateOf(false) }
    var capturedImageFile by remember { mutableStateOf<File?>(null) }
    var isCapturing by remember { mutableStateOf(false) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    // Add permission states based on Android version
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val storagePermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    val sheetState = rememberModalBottomSheetState()

    // Add state for PDF file
    var pdfFile by remember { mutableStateOf<File?>(null) }
    var showPdfPreview by remember { mutableStateOf(false) }

    // Add state for PDF preview
    var pdfPreviewBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // Add state for confirmation dialog
    var showConfirmationDialog by remember { mutableStateOf(false) }

    // Add state for file locations
    var imageFiles by remember { mutableStateOf<List<File>>(emptyList()) }
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedCourse by remember { mutableStateOf<Course?>(null) }

    // Add state for upload completed
    var isUploadComplete by remember { mutableStateOf(false) }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                try {
                    val mimeType = context.contentResolver.getType(uri)
                    Log.d("FilePicker", "Selected file MIME type: $mimeType")
                    
                    when {
                        mimeType?.startsWith("image/") == true -> {
                            // Handle image files
                            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                            val imageFileName = "IMG_${timeStamp}.jpg"
                            val imageFile = File(context.filesDir, "images/$imageFileName")
                            
                            // Create images directory if it doesn't exist
                            imageFile.parentFile?.mkdirs()
                            
                            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                FileOutputStream(imageFile).use { output ->
                                    inputStream.copyTo(output)
                                }
                            }
                            
                            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                            capturedPhotos = capturedPhotos + bitmap.asImageBitmap()
                            //imageFiles = imageFiles + imageFile
                            isFileUploaded = true
                            fileName = imageFileName
                            
                            Log.d("FilePicker", "Image saved to: ${imageFile.absolutePath}")
                        }
                        mimeType == "application/pdf" -> {
                            // Handle PDF files
                            selectedFiles = selectedFiles + uri
                            isFileUploaded = true
                            
                            // Get the original filename
                            val cursor = context.contentResolver.query(uri, null, null, null, null)
                            cursor?.use {
                                if (it.moveToFirst()) {
                                    val displayNameIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                                    if (displayNameIndex != -1) {
                                        fileName = it.getString(displayNameIndex)
                                    }
                                }
                            }
                            
                            if (fileName == null) {
                                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                                fileName = "Document_${timeStamp}.pdf"
                            }
                            
                            // Create a copy in app's private storage
                            val newPdfFile = File(context.filesDir, "documents/$fileName")
                            
                            // Create documents directory if it doesn't exist
                            newPdfFile.parentFile?.mkdirs()
                            
                            context.contentResolver.openInputStream(uri)?.use { input ->
                                FileOutputStream(newPdfFile).use { output ->
                                    input.copyTo(output)
                                }
                            }
                            
                            pdfFile = newPdfFile
                            Log.d("FilePicker", "PDF saved to: ${newPdfFile.absolutePath}")
                            
                            // Generate PDF preview
                            val pfd = ParcelFileDescriptor.open(newPdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
                            val pdfRenderer = PdfRenderer(pfd)
                            
                            try {
                                val page = pdfRenderer.openPage(0)
                                val scale = minOf(
                                    150.dp.value * context.resources.displayMetrics.density / page.width,
                                    200.dp.value * context.resources.displayMetrics.density / page.height
                                )
                                
                                val bitmap = Bitmap.createBitmap(
                                    (page.width * scale).toInt(),
                                    (page.height * scale).toInt(),
                                    Bitmap.Config.ARGB_8888
                                )
                                
                                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                                pdfPreviewBitmap = bitmap.asImageBitmap()
                                page.close()
                            } finally {
                                pdfRenderer.close()
                                pfd.close()
                            }
                            fileUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                newPdfFile
                            )
                            Log.d("FilePicker", "PDF saved to: ${newPdfFile.absolutePath}")
                        }
                        else -> {
                            Log.e("FilePicker", "Unsupported file type: $mimeType")
                            imageLoadingError = "Unsupported file type: $mimeType"
                        }
                    }
                } catch (e: Exception) {
                    Log.e("FilePicker", "Error processing file: ${e.message}")
                    imageLoadingError = e.message
                }
            }
        }
    )

    // A function that generates a unique filename
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    var hasNavigatedAfterUpload by remember { mutableStateOf(false) }

    // Handle upload state changes
    LaunchedEffect(uploadState) {
        Log.d("DocumentUpload", "Upload state changed to: $uploadState")
        when (uploadState) {
            is UploadState.Success -> {
                Log.d("DocumentUpload", "Upload success, hasNavigatedAfterUpload=$hasNavigatedAfterUpload")
                // Only navigate if we haven't already
                if (!hasNavigatedAfterUpload) {
                    // Mark upload as complete
                    isUploadComplete = true
                    // Navigate back after small delay to show completion
                    kotlinx.coroutines.delay(1000)
                    Log.d("DocumentUpload", "Navigating after successful upload")
                    onUploadComplete((uploadState as UploadState.Success).documentId)
                    hasNavigatedAfterUpload = true
                }
            }
            is UploadState.Loading -> {
                isUploading = true
                currentUploadStatus = "Uploading... ${(uploadState as UploadState.Loading).progress}%"
                uploadProgress = (uploadState as UploadState.Loading).progress.toFloat() / 100f
            }
            is UploadState.Error -> {
                isUploading = false
                currentUploadStatus = "Error: ${(uploadState as UploadState.Error).message}"
            }
            else -> {}
        }
    }

    // Reset the flag when returning to the screen
    LaunchedEffect(Unit) {
        Log.d("DocumentUpload", "Screen mounted, resetting navigation flag")
        hasNavigatedAfterUpload = false
        // If available, try to reset the upload state in the ViewModel too
         docUploadViewModel.resetUploadState()
    }

    LaunchedEffect(Unit) {
        isUploading = false
        isUploadComplete = false
        uploadProgress = 0f
        currentUploadStatus = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.UploadDocument)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                    }
                    ) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
        Box(modifier = Modifier.fillMaxSize()) {
            // Main content 
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Upload box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp).padding(top=16.dp)
                            .border(
                                width = 1.dp,
                                color = if (isFileUploaded) MaterialTheme.colorScheme.primary
                                else Color.Gray.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                showUploadOptions = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (capturedPhotos.isNotEmpty() || selectedFiles.isNotEmpty() || fileUri != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Show captured photos
                                capturedPhotos.forEach { photo ->
                                    Image(
                                        bitmap = photo,
                                        contentDescription = "Captured Image",
                                        modifier = Modifier
                                            .height(200.dp)
                                            .width(150.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                // Show PDF files
                                selectedFiles.forEach { uri ->
                                    val mimeType = context.contentResolver.getType(uri)
                                    if (mimeType == "application/pdf") {
                                        Box(
                                            modifier = Modifier
                                                .height(200.dp)
                                                .width(150.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = RoundedCornerShape(8.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (pdfPreviewBitmap != null) {
                                                Image(
                                                    bitmap = pdfPreviewBitmap!!,
                                                    contentDescription = "PDF Preview",
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Fit
                                                )
                                            } else {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Icon(
                                                        Icons.Default.Description,
                                                        contentDescription = "PDF File",
                                                        tint = MaterialTheme.colorScheme.primary
                                                    )
                                                    Text(
                                                        fileName ?: "PDF Document",
                                                        color = MaterialTheme.colorScheme.primary,
                                                        textAlign = TextAlign.Center,
                                                        maxLines = 2,
                                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                                        modifier = Modifier.padding(horizontal = 8.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // Show single PDF file URI if present
                                fileUri?.let { uri ->
                                    Box(
                                        modifier = Modifier
                                            .height(200.dp)
                                            .width(150.dp)
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (pdfPreviewBitmap != null) {
                                            Image(
                                                bitmap = pdfPreviewBitmap!!,
                                                contentDescription = "PDF Preview",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Fit
                                            )
                                        } else {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Description,
                                                    contentDescription = "PDF File",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                                Text(
                                                    fileName ?: "PDF Document",
                                                    color = MaterialTheme.colorScheme.primary,
                                                    textAlign = TextAlign.Center,
                                                    maxLines = 2,
                                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                                    modifier = Modifier.padding(horizontal = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                // Add button to take more photos or add more files
                                Box(
                                    modifier = Modifier
                                        .height(200.dp)
                                        .width(150.dp)
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            if (cameraPermissionState.status.isGranted && storagePermissionState.status.isGranted) {
                                                showCamera = true
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "Add More",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            "Add More",
                                            color = MaterialTheme.colorScheme.primary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Upload",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    stringResource(R.string.ClickToUploadOrTakePhoto),
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    stringResource(R.string.MaxFileSize),
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                item {
                    CustomTextField(
                        value = subject,
                        onValueChange = { input ->
                            // Only allow capital English letters
                            val filtered = input.filter { it in 'A'..'Z' }
                            subject = filtered
                        },
                        label = stringResource(R.string.SubjectCapitalLettersOnly),
                        placeholder = stringResource(R.string.EnterSubjectExample),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    CustomTextField(
                        value = subjectCode,
                        onValueChange = { input ->
                            // Only allow numbers
                            val filtered = input.filter { it.isDigit() }
                            subjectCode = filtered
                        },
                        label = stringResource(R.string.SubjectCodeNumbersOnly),
                        placeholder = stringResource(R.string.EnterSubjectCode),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Column {
                        DropdownList(
                            title = stringResource(R.string.Year),
                            selectedItem = year.ifEmpty { "Select year" },
                            onItemSelected = {
                                year = it
                                isYearExpanded = false
                            },
                            expanded = isYearExpanded,
                            onExpandedChange = { isYearExpanded = it },
                            content = years
                        )
                        Spacer(modifier = Modifier.height(yearSpacing))
                    }
                }

                item {
                    Column {
                        DropdownList(
                            title = stringResource(R.string.Semester),
                            selectedItem = semester.ifEmpty { "Select semester" },
                            onItemSelected = {
                                semester = it
                                isSemesterExpanded = false
                            },
                            expanded = isSemesterExpanded,
                            onExpandedChange = { isSemesterExpanded = it },
                            content = semesters
                        )
                        Spacer(modifier = Modifier.height(semesterSpacing))
                    }
                }


                item {
                    CustomTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = stringResource(R.string.Title),
                        placeholder = stringResource(R.string.EnterDocumentTitle),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    CustomTextField(
                        value = professor,
                        onValueChange = { professor = it },
                        label = stringResource(R.string.Professor),
                        placeholder = stringResource(R.string.EnterProfessorName),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    CustomTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = stringResource(R.string.Description),
                        placeholder = stringResource(R.string.EnterDocumentDescription),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Button(
                        onClick = {
                            // Validate form before showing confirmation
                            if (userId != null && title.isNotEmpty() && subject.isNotEmpty() && 
                                subjectCode.isNotEmpty() && (capturedPhotos.isNotEmpty() || selectedFiles.isNotEmpty() || fileUri != null)) {
                                
                                // Show confirmation dialog instead of uploading directly
                                showConfirmationDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isUploading && userId != null && title.isNotEmpty() && 
                                  subject.isNotEmpty() && subjectCode.isNotEmpty() && 
                                  (capturedPhotos.isNotEmpty() || selectedFiles.isNotEmpty() || fileUri != null),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Proceed to Upload")
                    }
                }

                // If form is not valid and user has attempted to submit, show error message
                item {
                    if (!isFormValid) {
                        val errorMessage = buildString {
                            if (!isFileUploaded) append(stringResource(R.string.PleaseUploadAFile) + "\n")
                            if (subject.isBlank()) append(stringResource(R.string.SubjectIsRequired) + "\n")
                            if (subjectCode.isBlank()) append(stringResource(R.string.SubjectCodeIsRequired) + "\n")
                            if (year.isBlank()) append(stringResource(R.string.YearIsRequired) + "\n")
                            if (semester.isBlank()) append(stringResource(R.string.SemesterIsRequired) + "\n")
                            if (title.isBlank()) append(stringResource(R.string.TitleIsRequired) + "\n")
                        }

                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Upload progress overlay
            if (isUploading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .clickable(enabled = false) { /* Disable clicks behind the overlay */ },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .width(300.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(24.dp)
                    ) {
                        if (isUploadComplete) {
                            // Show check mark or success icon
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Upload Complete",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Upload Complete!",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                text = "Uploading Document",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LinearProgressIndicator(
                                progress = { uploadProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentUploadStatus,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Upload Options Bottom Sheet
        if (showUploadOptions) {
            ModalBottomSheet(
                onDismissRequest = { showUploadOptions = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ChooseUploadMethod),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    TextButton(
                        onClick = {
                            showUploadOptions = false
                            if (cameraPermissionState.status.isGranted && storagePermissionState.status.isGranted) {
                                showCamera = true
                            } else {
                                if (!cameraPermissionState.status.isGranted) {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                                if (!storagePermissionState.status.isGranted) {
                                    storagePermissionState.launchPermissionRequest()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(R.string.TakePhoto),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(stringResource(R.string.TakePhoto))
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    TextButton(
                        onClick = {
                            showUploadOptions = false
                            filePickerLauncher.launch("application/pdf")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(R.string.ChoosePDF),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(stringResource(R.string.ChoosePDF))
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    TextButton(
                        onClick = {
                            showUploadOptions = false
                            filePickerLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(R.string.ChooseImage),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(stringResource(R.string.ChooseImage))
                        }
                    }
                }
            }
        }


        // Add the camera dialog that handle photo taking
        if (showCamera) {
            AlertDialog(
                onDismissRequest = {
                    if (!isCapturing) {
                        showCamera = false
                    }
                },
                title = { Text(stringResource(R.string.TakePhoto)) },
                text = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CameraView(
                            onImageCaptured = { capture ->
                                imageCapture = capture
                            }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (!isCapturing && imageCapture != null) {
                                val file = createImageFile()
                                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                                isCapturing = true
                                imageCapture?.takePicture(
                                    outputOptions,
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                            try {
                                                // Load the bitmap from the file
                                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)

                                                // Fix image orientation
                                                val exif = ExifInterface(file.absolutePath)
                                                val orientation = exif.getAttributeInt(
                                                    ExifInterface.TAG_ORIENTATION,
                                                    ExifInterface.ORIENTATION_NORMAL
                                                )

                                                val matrix = Matrix()
                                                when (orientation) {
                                                    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                                                    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                                                    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                                                }

                                                val rotatedBitmap = Bitmap.createBitmap(
                                                    bitmap,
                                                    0,
                                                    0,
                                                    bitmap.width,
                                                    bitmap.height,
                                                    matrix,
                                                    true
                                                )

                                                // Add the new photo to the list
                                                capturedPhotos = capturedPhotos + rotatedBitmap.asImageBitmap()
                                                isFileUploaded = true
                                                imageLoadingError = null

                                                Log.d("ImageCapture", "Image loaded and rotated successfully from file: ${file.absolutePath}")

                                                // Save to device gallery
                                                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                                                val galleryFileName = "USTrade_${timeStamp}.jpg"

                                                val galleryValues = ContentValues().apply {
                                                    put(Media.DISPLAY_NAME, galleryFileName)
                                                    put(Media.MIME_TYPE, "image/jpeg")
                                                    put(Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/USTrade")
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                        put(Media.IS_PENDING, 1)
                                                    }
                                                }

                                                val galleryUri = context.contentResolver.insert(
                                                    Media.EXTERNAL_CONTENT_URI,
                                                    galleryValues
                                                ) ?: throw IllegalStateException("Failed to create new MediaStore record.")

                                                // Write the bitmap to the gallery file
                                                context.contentResolver.openOutputStream(galleryUri)?.use { outputStream ->
                                                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                                }

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                    galleryValues.clear()
                                                    galleryValues.put(Media.IS_PENDING, 0)
                                                    context.contentResolver.update(galleryUri, galleryValues, null, null)
                                                }

                                                // Notify the media scanner
                                                MediaScannerConnection.scanFile(
                                                    context,
                                                    arrayOf(galleryUri.path),
                                                    arrayOf("image/jpeg"),
                                                    null
                                                )

                                            } catch (e: Exception) {
                                                Log.e("ImageCapture", "Error processing image: ${e.message}")
                                                imageLoadingError = e.message
                                            } finally {
                                                isCapturing = false
                                                showCamera = false
                                            }
                                        }

                                        override fun onError(exception: ImageCaptureException) {
                                            Log.e("Camera", "Error capturing image", exception)
                                            isCapturing = false
                                            showCamera = false
                                        }
                                    }
                                )
                            }
                        },
                        enabled = !isCapturing && imageCapture != null
                    ) {
                        Text(stringResource(R.string.TakePhoto))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            if (!isCapturing) {
                                showCamera = false
                            }
                        },
                        enabled = !isCapturing
                    ) {
                        Text(stringResource(R.string.Cancel))
                    }
                }
            )
        }

        // Show permission rationale dialogs if needed
        if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
            AlertDialog(
                onDismissRequest = { /* Don't dismiss */ },
                title = { Text(stringResource(R.string.CameraPermissionRequired)) },
                text = { Text(stringResource(R.string.CameraPermissionMessage)) },
                confirmButton = {
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text(stringResource(R.string.GrantPermission))
                    }
                },
                dismissButton = {
                    Button(onClick = { /* Handle denial */ }) {
                        Text(stringResource(R.string.Cancel))
                    }
                }
            )
        }

        if (!storagePermissionState.status.isGranted && storagePermissionState.status.shouldShowRationale) {
            AlertDialog(
                onDismissRequest = { /* Don't dismiss */ },
                title = { Text(stringResource(R.string.StoragePermissionRequired)) },
                text = { Text(stringResource(R.string.StoragePermissionMessage)) },
                confirmButton = {
                    Button(onClick = { storagePermissionState.launchPermissionRequest() }) {
                        Text(stringResource(R.string.GrantPermission))
                    }
                },
                dismissButton = {
                    Button(onClick = { /* Handle denial */ }) {
                        Text(stringResource(R.string.Cancel))
                    }
                }
            )
        }

        // Add confirmation dialog
        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = { Text(stringResource(R.string.ConfirmUpload), fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 8.dp)
                    ) {
                        // Subject Information
                        Text(stringResource(R.string.SubjectInformation), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(stringResource(R.string.Subject) + ": ", fontWeight = FontWeight.Bold)
                            Text(subject)
                        }
                        Row {
                            Text(stringResource(R.string.SubjectCode) + ": ", fontWeight = FontWeight.Bold)
                            Text(subjectCode)
                        }
                        Row {
                            Text(stringResource(R.string.Year) + ": ", fontWeight = FontWeight.Bold)
                            Text(year)
                        }
                        Row {
                            Text(stringResource(R.string.Semester) + ": ", fontWeight = FontWeight.Bold)
                            Text(semester)
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Document Information
                        Text(stringResource(R.string.DocumentInformation), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(stringResource(R.string.Title) + ": ", fontWeight = FontWeight.Bold)
                            Text(title)
                        }
                        Row {
                            Text(stringResource(R.string.Professor) + ": ", fontWeight = FontWeight.Bold)
                            Text(professor)
                        }
                        if (description.isNotBlank()) {
                            Row {
                                Text(stringResource(R.string.Description) + ": ", fontWeight = FontWeight.Bold)
                                Text(description)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Uploaded Files
                        Text(stringResource(R.string.UploadedFiles), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        if (imageFiles.isNotEmpty()) {
                            Text(stringResource(R.string.Images) + ":", fontWeight = FontWeight.Bold)
                            imageFiles.forEachIndexed { index, file ->
                                Column {
                                    Text(stringResource(R.string.ImageNumber, index + 1, file.name))
                                    Text(
                                        stringResource(R.string.Location, file.absolutePath),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                        
                        if (pdfFile != null) {
                            Text(stringResource(R.string.PDF) + ":", fontWeight = FontWeight.Bold)
                            Column {
                                Text(pdfFile?.name ?: stringResource(R.string.UntitledPDF))
                                Text(
                                    stringResource(R.string.Location, pdfFile?.absolutePath ?: ""),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (userId != null) {
                                // Create document object using ViewModel
                                val document = docUploadViewModel.createDocumentObject(
                                    title = title,
                                    description = description,
                                    userId = userId,
                                    subject = subject,
                                    subjectCode = subjectCode,
                                    year = year,
                                    semester = semester
                                )
                                
                                // Collect all file URIs to upload
                                val allFileUris = mutableListOf<Uri>()
                                
                                // Add all selected files
                                allFileUris.addAll(selectedFiles)
                                
                                // Add single file URI if present and not already in list
                                fileUri?.let { 
                                    if (!allFileUris.contains(it)) {
                                        allFileUris.add(it)
                                    }
                                }
                                
                                // For captured photos, save as files and get URIs
                                if (capturedPhotos.isNotEmpty()) {
                                    try {
                                        // Create directory for photos if it doesn't exist
                                        val photosDir = File(context.cacheDir, "photos").apply { mkdirs() }
                                        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                                        
                                        // Process each captured photo
                                        capturedPhotos.forEachIndexed { index, imageBitmap ->
                                            // Create a file for the image
                                            val photoFile = File(photosDir, "PHOTO_${timeStamp}_${index}.jpg")
                                            
                                            // Save the bitmap to file
                                            FileOutputStream(photoFile).use { out ->
                                                imageBitmap.asBitmap().compress(Bitmap.CompressFormat.JPEG, 90, out)
                                            }
                                            
                                            // Get URI using FileProvider
                                            val photoUri = FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.provider",
                                                photoFile
                                            )
                                            
                                            // Add to list of URIs to upload
                                            allFileUris.add(photoUri)
                                        }
                                    } catch (e: Exception) {
                                        Log.e("DocumentUpload", "Error saving photos: ${e.message}")
                                        // Show error message to user if needed
                                        currentUploadStatus = "Error preparing photos: ${e.message}"
                                    }
                                }
                                
                                // Upload all files together as one document
                                if (allFileUris.isNotEmpty()) {
                                    try {
                                        // Start upload
                                        docUploadViewModel.uploadDocument(
                                            context = context,
                                            fileUris = allFileUris,
                                            document = document
                                        )
                                        
                                        // Close dialog and show upload progress
                                        showConfirmationDialog = false
                                        isUploading = true
                                        currentUploadStatus = "Starting upload..."
                                        uploadProgress = 0.1f
                                    } catch (e: Exception) {
                                        Log.e("DocumentUpload", "Error starting upload: ${e.message}")
                                        currentUploadStatus = "Error: ${e.message}"
                                    }
                                } else {
                                    // No files to upload
                                    currentUploadStatus = "No files selected for upload"
                                }
                            }
                        },
                        enabled = !isUploading && userId != null && 
                                 (capturedPhotos.isNotEmpty() || selectedFiles.isNotEmpty() || fileUri != null)
                    ) {
                        Text("Confirm Upload")
                    }
                }
            )
        }
    }
}