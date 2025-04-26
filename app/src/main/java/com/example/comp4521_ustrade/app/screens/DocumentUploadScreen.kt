package com.example.comp4521_ustrade.app.screens

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.comp4521_ustrade.R
import com.example.comp4521_ustrade.app.components.CustomTextField
import com.example.comp4521_ustrade.app.components.DropdownList
import com.example.comp4521_ustrade.app.viewmodel.NavViewModel
import com.example.comp4521_ustrade.ui.theme.USTBlue
import com.example.comp4521_ustrade.ui.theme.USTBlue_dark
import androidx.compose.material3.AlertDialog
import androidx.core.content.ContextCompat
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.comp4521_ustrade.app.screens.camera.CameraView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import android.Manifest
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import android.os.Environment
import android.content.ContentValues
import android.provider.MediaStore
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.content.ContentResolver
import android.provider.MediaStore.Images.Media
import android.media.ExifInterface
import android.graphics.Matrix
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.TextButton
import androidx.compose.material3.Divider
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DocumentUploadScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    navViewModel: NavViewModel
) {
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

    // Add states for dropdown expansions
    var isSubjectExpanded by remember { mutableStateOf(false) }
    var isSubjectCodeExpanded by remember { mutableStateOf(false) }
    var isYearExpanded by remember { mutableStateOf(false) }
    var isSemesterExpanded by remember { mutableStateOf(false) }

    // Add dropdown options
    val subjects = listOf("Science", "Engineering", "Business")
    val subjectCodes = listOf(
        "COMP", "CPEG", "CSIT", "IEDA", "MECH", "CIVL", "ELEC"
    )
    val years = listOf("2024", "2023", "2022", "2021")
    val semesters = listOf("Fall", "Spring", "Summer", "Winter")

    // Calculate dynamic spacing based on dropdown state
    val subjectSpacing = if (isSubjectExpanded) (subjects.size * 48).dp else 8.dp
    val subjectCodeSpacing = if (isSubjectCodeExpanded) (subjectCodes.size * 48).dp else 8.dp
    val yearSpacing = if (isYearExpanded) (years.size * 48).dp else 8.dp
    val semesterSpacing = if (isSemesterExpanded) (semesters.size * 48).dp else 8.dp

    // Check if all required fields are filled and file is uploaded
    val isFormValid = remember(subject, subjectCode, year, semester, title, isFileUploaded) {
        subject.isNotBlank() &&
                subjectCode.isNotBlank() &&
                year.isNotBlank() &&
                semester.isNotBlank() &&
                title.isNotBlank() &&
                isFileUploaded
    }

    val context = LocalContext.current

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

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val mimeType = context.contentResolver.getType(uri)
                Log.d("FilePicker", "Selected file MIME type: $mimeType")

                when {
                    mimeType?.startsWith("image/") == true -> {
                        // Handle image files
                        context.contentResolver.openInputStream(uri)?.use { inputStream ->
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            capturedPhotos = capturedPhotos + bitmap.asImageBitmap()
                            isFileUploaded = true
                            fileName = "Image_${System.currentTimeMillis()}.jpg"
                        }
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
                            fileName = "Document_${System.currentTimeMillis()}.pdf"
                        }

                        // Create a copy in app's private storage
                        val newPdfFile = File(context.filesDir, fileName!!)

                        context.contentResolver.openInputStream(uri)?.use { input ->
                            FileOutputStream(newPdfFile).use { output ->
                                input.copyTo(output)
                            }
                        }

                        pdfFile = newPdfFile
                        Log.d("FilePicker", "PDF file saved: ${newPdfFile.absolutePath}")
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

    // Create a function to generate a unique filename
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.UploadDocument)) },
                navigationIcon = {
                    IconButton(onClick = {
                        navViewModel.setSelectedScreen(Screens.Home)
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
                        .height(240.dp)
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
                    if (capturedPhotos.isNotEmpty() || pdfFile != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .horizontalScroll(rememberScrollState())
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
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

                            pdfFile?.let { file ->
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
                                            val uri = FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.provider",
                                                file
                                            )
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                setDataAndType(uri, "application/pdf")
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            }
                                            context.startActivity(intent)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Description,
                                            contentDescription = "PDF Preview",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            "View PDF",
                                            color = MaterialTheme.colorScheme.primary,
                                            textAlign = TextAlign.Center
                                        )
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
                                            showUploadOptions = true
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
                Column {
                    DropdownList(
                        title = stringResource(R.string.Subject),
                        selectedItem = subject.ifEmpty { "Select subject" },
                        onItemSelected = {
                            subject = it
                            isSubjectExpanded = false
                        },
                        expanded = isSubjectExpanded,
                        onExpandedChange = { isSubjectExpanded = it },
                        content = subjects
                    )
                    Spacer(modifier = Modifier.height(subjectSpacing))
                }
            }

            item {
                Column {
                    DropdownList(
                        title = stringResource(R.string.SubjectCode),
                        selectedItem = subjectCode.ifEmpty { "Select subject code" },
                        onItemSelected = {
                            subjectCode = it
                            isSubjectCodeExpanded = false
                        },
                        expanded = isSubjectCodeExpanded,
                        onExpandedChange = { isSubjectCodeExpanded = it },
                        content = subjectCodes
                    )
                    Spacer(modifier = Modifier.height(subjectCodeSpacing))
                }
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
                    onClick = { /* Handle upload */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid
                ) {
                    Text(stringResource(R.string.Upload))
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
                        text = "Choose Upload Method",
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
                                contentDescription = "Take Photo",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Take Photo")
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
                                contentDescription = "Choose PDF",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Choose PDF")
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
                                contentDescription = "Choose Image",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Choose Image")
                        }
                    }
                }
            }
        }


        // Add the camera dialog
        if (showCamera) {
            AlertDialog(
                onDismissRequest = {
                    if (!isCapturing) {
                        showCamera = false
                    }
                },
                title = { Text("Take Photo") },
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
                        Text("Take Photo")
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
                        Text("Cancel")
                    }
                }
            )
        }

        // Show permission rationale dialogs if needed
        if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
            AlertDialog(
                onDismissRequest = { /* Don't dismiss */ },
                title = { Text("Camera Permission Required") },
                text = { Text("This app needs camera access to take photos. Please grant the permission in Settings.") },
                confirmButton = {
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text("Grant Permission")
                    }
                },
                dismissButton = {
                    Button(onClick = { /* Handle denial */ }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (!storagePermissionState.status.isGranted && storagePermissionState.status.shouldShowRationale) {
            AlertDialog(
                onDismissRequest = { /* Don't dismiss */ },
                title = { Text("Storage Permission Required") },
                text = { Text("This app needs storage access to save photos. Please grant the permission in Settings.") },
                confirmButton = {
                    Button(onClick = { storagePermissionState.launchPermissionRequest() }) {
                        Text("Grant Permission")
                    }
                },
                dismissButton = {
                    Button(onClick = { /* Handle denial */ }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

private fun Uri.toBitmap(context: Context): android.graphics.Bitmap {
    return context.contentResolver.openInputStream(this)?.use { inputStream ->
        BitmapFactory.decodeStream(inputStream)
    } ?: throw IllegalStateException("Could not load image from URI")
} 