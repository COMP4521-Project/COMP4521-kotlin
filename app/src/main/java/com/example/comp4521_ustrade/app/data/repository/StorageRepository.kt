package com.example.comp4521_ustrade.app.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.File

class StorageRepository {
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference

    suspend fun uploadFile(
        file: File,
        documentId: String,
        fileName: String,
        onProgress: ((Double) -> Unit)? = null
    ): String {
        return try {
            val fileRef = storageRef.child("documents/$documentId/$fileName")
            
            // Create metadata with content type
            val metadata = StorageMetadata.Builder()
                .setContentType(getMimeType(fileName))
                .build()

            // Upload file with metadata
            val uploadTask = fileRef.putFile(Uri.fromFile(file), metadata)
            
            // Monitor upload progress if callback provided
            onProgress?.let { callback ->
                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    callback(progress)
                }
            }

            // Wait for upload to complete
            uploadTask.await()
            
            // Get download URL
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun uploadMultipleFiles(
        files: List<File>,
        documentId: String,
        onProgress: ((Int, Double) -> Unit)? = null
    ): List<String> {
        return try {
            files.mapIndexed { index, file ->
                val fileName = file.name
                val fileRef = storageRef.child("documents/$documentId/$fileName")
                
                // Create metadata with content type
                val metadata = StorageMetadata.Builder()
                    .setContentType(getMimeType(fileName))
                    .build()

                // Upload file with metadata
                val uploadTask = fileRef.putFile(Uri.fromFile(file), metadata)
                
                // Monitor upload progress if callback provided
                onProgress?.let { callback ->
                    uploadTask.addOnProgressListener { taskSnapshot ->
                        val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                        callback(index, progress)
                    }
                }

                // Wait for upload to complete
                uploadTask.await()
                
                // Get download URL
                fileRef.downloadUrl.await().toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun deleteFile(documentId: String, fileName: String) {
        try {
            val fileRef = storageRef.child("documents/$documentId/$fileName")
            fileRef.delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun uploadProfileImage(
        imageUri: Uri,
        userId: String,
        onProgress: ((Double) -> Unit)? = null
    ): String {
        return try {
            val profilePath = "profile_images/$userId.jpg"
            val storageRef = storage.reference.child(profilePath)
            
            // Create metadata with content type
            val metadata = StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build()

            // Upload file with metadata
            val uploadTask = storageRef.putFile(imageUri, metadata)
            
            // Monitor upload progress if callback provided
            onProgress?.let { callback ->
                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    callback(progress)
                }
            }

            // Wait for upload to complete
            uploadTask.await()
            
            // Get download URL
            storageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // Helper function to get MIME type based on file extension
    private fun getMimeType(fileName: String): String {
        return when {
            fileName.endsWith(".pdf") -> "application/pdf"
            fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> "image/jpeg"
            fileName.endsWith(".png") -> "image/png"
            else -> "application/octet-stream"
        }
    }

    // Flow-based progress monitoring
    fun monitorUploadProgress(uploadTask: UploadTask): Flow<Double> = callbackFlow {
        val listener = uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
            trySend(progress)
        }
        awaitClose { listener }
    }
} 