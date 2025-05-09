package com.example.comp4521_ustrade.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.webkit.MimeTypeMap
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object DocumentUtils {
    
    /**
     * Processes a document and generates a cover image
     * Returns the URL of the cover image, or null if unable to generate
     */
    suspend fun processCoverImage(context: Context, fileUri: Uri, userId: String, documentId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val mimeType = getMimeType(context, fileUri)
                
                // Handle different file types
                when {
                    mimeType?.startsWith("application/pdf") == true -> {
                        // Process PDF
                        val bitmap = extractPdfFirstPage(context, fileUri)
                        if (bitmap != null) {
                            return@withContext uploadCoverImage(bitmap, userId, documentId)
                        }
                    }
                    mimeType?.startsWith("image/") == true -> {
                        // It's already an image, use as-is
                        return@withContext uploadOriginalAsImage(context, fileUri, userId, documentId)
                    }
                    // Other document types could be handled here
                    // For example, for DOCX or PPT, you might use a default icon based on type
                }
                
                // If we couldn't create a cover, return null
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    
    private fun getMimeType(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        return contentResolver.getType(uri) ?: MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(uri.lastPathSegment?.substringAfterLast('.', ""))
    }
    
    private suspend fun extractPdfFirstPage(context: Context, pdfUri: Uri): Bitmap? {
        try {
            // Copy the file to a temp location we can access with file descriptor
            val tempFile = File(context.cacheDir, "temp_pdf_${System.currentTimeMillis()}.pdf")
            context.contentResolver.openInputStream(pdfUri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            // Use PDF renderer to capture first page
            val fileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fileDescriptor)
            
            if (renderer.pageCount > 0) {
                val page = renderer.openPage(0)
                // Create bitmap with reasonable dimensions for a cover
                val bitmap = Bitmap.createBitmap(
                    (page.width * 1.5).toInt(), 
                    (page.height * 1.5).toInt(),
                    Bitmap.Config.ARGB_8888
                )
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                renderer.close()
                fileDescriptor.close()
                tempFile.delete()
                return bitmap
            }
            
            renderer.close()
            fileDescriptor.close()
            tempFile.delete()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    private suspend fun uploadCoverImage(bitmap: Bitmap, userId: String, documentId: String): String? {
        return try {
            val baos = ByteArrayOutputStream()
            // Compress image to reduce size
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val data = baos.toByteArray()
            
            val coverPath = "covers/$userId/$documentId.jpg"
            val storageRef = FirebaseStorage.getInstance().reference.child(coverPath)
            
            storageRef.putBytes(data).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private suspend fun uploadOriginalAsImage(context: Context, imageUri: Uri, userId: String, documentId: String): String? {
        return try {
            val coverPath = "covers/$userId/$documentId.jpg"
            val storageRef = FirebaseStorage.getInstance().reference.child(coverPath)
            
            // Upload the image file directly
            storageRef.putFile(imageUri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
} 