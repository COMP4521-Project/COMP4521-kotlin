import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comp4521_ustrade.app.data.dao.Document
import com.example.comp4521_ustrade.app.data.dao.UploadDocument
import com.example.comp4521_ustrade.app.data.repository.DocumentRepository
import com.example.comp4521_ustrade.app.data.repository.UserRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.math.max
import android.os.ParcelFileDescriptor
import android.graphics.pdf.PdfRenderer

class DocumentUploadViewModel : ViewModel() {
    private val documentRepository = DocumentRepository()
    private val userRepository = UserRepository()
    private val storage = FirebaseStorage.getInstance()
    
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Initial)
    val uploadState: StateFlow<UploadState> = _uploadState
    
    fun uploadDocument(
        context: Context,
        fileUris: List<Uri>,
        document: Document
    ) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            
            try {
                val documentId = document.id
                val userId = document.uploaded_by
                var documentThumbnailUrl: String? = null
                
                // Process and upload each file
                val uploadDocuments = fileUris.mapIndexed { index, fileUri ->
                    // 1. Upload the actual document file
                    val fileExtension = getFileExtension(context, fileUri)
                    val originalFileName = getOriginalFileName(context, fileUri)
                    val fileName = "file_${System.currentTimeMillis()}.$fileExtension"
                    val documentPath = "documents/$userId/$documentId/$fileName"
                    val storageRef = storage.reference.child(documentPath)
                    
                    storageRef.putFile(fileUri).await()
                    val fileUrl = storageRef.downloadUrl.await().toString()
                    
                    // 2. Generate and upload cover image
                    val coverImageUrl = processAndUploadCoverImage(context, fileUri, userId, documentId)
                    
                    // 3. For the first file, also generate a thumbnail for the main document
                    if (index == 0) {
                        documentThumbnailUrl = processAndUploadThumbnail(context, fileUri, userId, documentId)
                    }
                    
                    // 4. Create UploadDocument with the simplified structure
                    UploadDocument(
                        file_url = fileUrl,
                        file_type = getMimeType(context, fileUri) ?: "application/octet-stream",
                        coverImageUrl = coverImageUrl,
                        document_name = originalFileName
                    )
                }
                
                // 5. Create updated document with uploaded files
                val updatedDocument = document.copy(
                    upload_documents = uploadDocuments,
                    thumbnailUrl = documentThumbnailUrl
                )
                
                // 6. Save to Firestore
                documentRepository.addDocument(updatedDocument)
                
                // 7. Update user's uploaded documents
                userRepository.addUploadedDocumentToUser(userId, documentId)
                userRepository.increaseUserUpload(userId)
                
                _uploadState.value = UploadState.Success(documentId)
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    fun uploadDocumentWithAllContent(
        context: Context,
        document: Document,
        fileUris: List<Uri>,
        bitmapFiles: List<Bitmap>
    ) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading
            
            try {
                val documentId = document.id
                val userId = document.uploaded_by
                val uploadDocuments = mutableListOf<UploadDocument>()
                var thumbnailUrl: String? = null
                
                // First, process and upload file URIs
                fileUris.forEachIndexed { index, fileUri ->
                    // Get file info
                    val originalFileName = getOriginalFileName(context, fileUri)
                    val mimeType = getMimeType(context, fileUri) ?: "application/octet-stream"
                    val fileExtension = getFileExtension(context, fileUri)
                    
                    // Upload the file
                    val fileName = "file_${index+1}_${System.currentTimeMillis()}.$fileExtension"
                    val documentPath = "documents/$userId/$documentId/$fileName"
                    val storageRef = storage.reference.child(documentPath)
                    
                    storageRef.putFile(fileUri).await()
                    val fileUrl = storageRef.downloadUrl.await().toString()
                    
                    // Generate cover image for the file
                    val coverImageUrl = processAndUploadCoverImage(context, fileUri, userId, documentId)
                    
                    // Generate thumbnail for the first file
                    if (index == 0 && thumbnailUrl == null) {
                        thumbnailUrl = processAndUploadThumbnail(context, fileUri, userId, documentId)
                    }
                    
                    // Add to upload documents
                    uploadDocuments.add(
                        UploadDocument(
                            file_url = fileUrl,
                            file_type = mimeType,
                            coverImageUrl = coverImageUrl,
                            document_name = originalFileName
                        )
                    )
                }
                
                // Next, process and upload bitmap files
                bitmapFiles.forEachIndexed { index, bitmap ->
                    // Generate filename
                    val fileName = "photo_${index+1}_${System.currentTimeMillis()}.jpg"
                    val documentPath = "documents/$userId/$documentId/$fileName"
                    
                    // Upload bitmap
                    val fileUrl = uploadBitmapToStorage(
                        bitmap, 
                        documentPath, 
                        90, // High quality
                        2048 // Good resolution
                    ) ?: throw Exception("Failed to upload photo")
                    
                    // Generate cover image (smaller version)
                    val coverImageUrl = uploadBitmapToStorage(
                        bitmap,
                        "covers/$userId/$documentId/photo_${index+1}.jpg",
                        85,
                        1024
                    )
                    
                    // Generate thumbnail for the first file if none exists yet
                    if (thumbnailUrl == null && index == 0) {
                        thumbnailUrl = uploadBitmapToStorage(
                            bitmap,
                            "thumbnails/$userId/$documentId.jpg",
                            75,
                            300
                        )
                    }
                    
                    // Add to upload documents
                    uploadDocuments.add(
                        UploadDocument(
                            file_url = fileUrl,
                            file_type = "image/jpeg",
                            coverImageUrl = coverImageUrl,
                            document_name = "Photo_${index+1}"
                        )
                    )
                }
                
                // Update document with all content
                val updatedDocument = document.copy(
                    upload_documents = uploadDocuments,
                    thumbnailUrl = thumbnailUrl
                )
                
                // Save to Firestore
                documentRepository.addDocument(updatedDocument)
                
                // Update user's uploaded documents
                userRepository.addUploadedDocumentToUser(userId, documentId)
                userRepository.increaseUserUpload(userId)
                
                _uploadState.value = UploadState.Success(documentId)
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    // Helper function to create a document object with basic info
    fun createDocumentObject(
        title: String,
        description: String,
        userId: String,
        subject: String,
        subjectCode: String,
        year: String = "",
        semester: String = ""
    ): Document {
        val documentId = UUID.randomUUID().toString()
        return Document(
            id = documentId,
            title = title,
            description = description,
            subject = subject,
            subjectCode = subjectCode,
            course = "${subject}${subjectCode}",
            year = year,
            semester = semester,
            uploaded_by = userId,
            upload_date = getCurrentDate(),
            thumbnailUrl = null,
            upload_documents = emptyList(), // Will be filled with uploaded documents
            downloadCount = 0,
            like_count = 0,
            dislike_count = 0,
        )
    }
    
    private suspend fun processAndUploadCoverImage(
        context: Context, 
        fileUri: Uri, 
        userId: String, 
        documentId: String
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val mimeType = getMimeType(context, fileUri)
                
                when {
                    mimeType?.startsWith("application/pdf") == true -> {
                        // Process PDF
                        val bitmap = extractPdfFirstPage(context, fileUri)
                        if (bitmap != null) {
                            return@withContext uploadBitmapToStorage(
                                bitmap, 
                                "covers/$userId/$documentId.jpg", 
                                90, 
                                1024
                            )
                        }
                    }
                    mimeType?.startsWith("image/") == true -> {
                        // It's already an image
                        return@withContext uploadImageToStorage(
                            context,
                            fileUri, 
                            "covers/$userId/$documentId.jpg",
                            1024
                        )
                    }
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    
    private suspend fun processAndUploadThumbnail(
        context: Context, 
        fileUri: Uri, 
        userId: String, 
        documentId: String
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val mimeType = getMimeType(context, fileUri)
                
                when {
                    mimeType?.startsWith("application/pdf") == true -> {
                        // Process PDF
                        val bitmap = extractPdfFirstPage(context, fileUri)
                        if (bitmap != null) {
                            // Lower quality, smaller size for thumbnail
                            return@withContext uploadBitmapToStorage(
                                bitmap, 
                                "thumbnails/$userId/$documentId.jpg", 
                                75, // Lower quality
                                300 // Smaller max dimension
                            )
                        }
                    }
                    mimeType?.startsWith("image/") == true -> {
                        // It's already an image, upload a compressed version
                        return@withContext uploadImageToStorage(
                            context,
                            fileUri, 
                            "thumbnails/$userId/$documentId.jpg",
                            300 // Smaller max dimension for thumbnail
                        )
                    }
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    
    private suspend fun uploadBitmapToStorage(
        bitmap: Bitmap, 
        path: String, 
        quality: Int,
        maxDimension: Int
    ): String? {
        return try {
            // Resize bitmap if needed
            val resizedBitmap = if (bitmap.width > maxDimension || bitmap.height > maxDimension) {
                val scale = maxDimension.toFloat() / max(bitmap.width, bitmap.height)
                val width = (bitmap.width * scale).toInt()
                val height = (bitmap.height * scale).toInt()
                Bitmap.createScaledBitmap(bitmap, width, height, true)
            } else bitmap
            
            val baos = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            val data = baos.toByteArray()
            
            val storageRef = FirebaseStorage.getInstance().reference.child(path)
            storageRef.putBytes(data).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private suspend fun uploadImageToStorage(
        context: Context,
        uri: Uri, 
        path: String,
        maxDimension: Int
    ): String? {
        return try {
            // Load and resize the bitmap first
            val bitmap = withContext(Dispatchers.IO) {
                val opts = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                var inputStream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream, null, opts)
                inputStream?.close()
                
                // Calculate sample size
                val width = opts.outWidth
                val height = opts.outHeight
                var inSampleSize = 1
                if (width > maxDimension || height > maxDimension) {
                    val halfWidth = width / 2
                    val halfHeight = height / 2
                    while ((halfWidth / inSampleSize) >= maxDimension || 
                           (halfHeight / inSampleSize) >= maxDimension) {
                        inSampleSize *= 2
                    }
                }
                
                // Decode with sample size
                opts.inJustDecodeBounds = false
                opts.inSampleSize = inSampleSize
                inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, opts)
                inputStream?.close()
                bitmap
            }
            
            // Upload the resized bitmap
            if (bitmap != null) {
                val quality = if (maxDimension > 500) 90 else 75
                uploadBitmapToStorage(bitmap, path, quality, maxDimension)
            } else {
                // Fallback: direct upload if bitmap processing fails
                val storageRef = FirebaseStorage.getInstance().reference.child(path)
                storageRef.putFile(uri).await()
                storageRef.downloadUrl.await().toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private suspend fun extractPdfFirstPage(context: Context, pdfUri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        try {
            // Create a temporary file to store the PDF
            val tempFile = File(context.cacheDir, "temp_pdf_${System.currentTimeMillis()}.pdf")
            context.contentResolver.openInputStream(pdfUri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Use PdfRenderer to render the first page
            val fileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)

            if (pdfRenderer.pageCount > 0) {
                val page = pdfRenderer.openPage(0)
                
                // Create a bitmap with appropriate dimensions
                val width = page.width * 2
                val height = page.height * 2
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                
                // Render the page to the bitmap
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                
                // Clean up resources
                page.close()
                pdfRenderer.close()
                fileDescriptor.close()
                tempFile.delete()
                
                return@withContext bitmap
            }
            
            // Clean up if no pages
            pdfRenderer.close()
            fileDescriptor.close()
            tempFile.delete()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun getMimeType(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        return contentResolver.getType(uri) ?: MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(uri.lastPathSegment?.substringAfterLast('.', ""))
    }
    
    private fun getFileExtension(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "pdf"
    }
    
    private fun getOriginalFileName(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        
        // Try to get the display name from the content resolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex("_display_name")
                if (displayNameIndex != -1) {
                    return it.getString(displayNameIndex)
                }
            }
        }
        
        // Fallback: extract name from URI path if available
        uri.lastPathSegment?.let { lastSegment ->
            if (lastSegment.contains("/")) {
                return lastSegment.substringAfterLast("/")
            }
            return lastSegment
        }
        
        // Last resort: create a generic name with timestamp
        return "document_${System.currentTimeMillis()}"
    }
    
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}

sealed class UploadState {
    object Initial : UploadState()
    object Loading : UploadState()
    data class Success(val documentId: String) : UploadState()
    data class Error(val message: String) : UploadState()
}

