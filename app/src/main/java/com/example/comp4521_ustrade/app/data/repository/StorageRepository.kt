package com.example.comp4521_ustrade.app.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.File

class StorageRepository {
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference

    suspend fun uploadFile(file: File, documentId: String, fileName: String): String {
        return try {
            val fileRef = storageRef.child("documents/$documentId/$fileName")
            fileRef.putFile(Uri.fromFile(file)).await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun uploadMultipleFiles(files: List<File>, documentId: String): List<String> {
        return try {
            files.mapIndexed { index, file ->
                val fileName = file.name
                val fileRef = storageRef.child("documents/$documentId/$fileName")
                fileRef.putFile(Uri.fromFile(file)).await()
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
} 