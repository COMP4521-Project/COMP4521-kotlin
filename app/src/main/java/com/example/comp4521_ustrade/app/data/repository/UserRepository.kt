package com.example.comp4521_ustrade.app.data.repository

import androidx.core.net.toUri
import com.example.comp4521_ustrade.app.data.dao.User
import com.example.comp4521_ustrade.app.data.dao.UserDocument
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("user")

    suspend fun addUser(user: User) {
        try {
            val userMap = hashMapOf(
                "uid" to user.uid,
                "first_name" to user.first_name,
                "last_name" to user.last_name,
                "fcm_token" to user.fcm_token,
                "profile_pic" to user.profile_pic?.toString(),
                "documents" to user.documents,
                "upload_count" to user.upload_count
            )
            usersCollection.document(user.uid).set(userMap).await()
        } catch (e: Exception) {
            // Handle error silently or log it
            e.printStackTrace()
            throw e
        }
    }

    suspend fun updateUser(uid: String, updates: Map<String, Any>) {
        try {
            usersCollection.document(uid).update(updates).await()
        } catch (e: Exception) {
            // Handle error silently or log it
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getUser(uid: String): User? {
        return try {
            val document = usersCollection.document(uid).get().await()
            if (document.exists()) {
                val data = document.data
                if (data != null) {
                    // Parse the documents field
                    val documentsMap = data["documents"] as? Map<*, *>
                    val userDocument = documentsMap?.let {
                        UserDocument(
                            uploaded = (it["uploaded"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                            downloaded = emptyList(),
                            bookmarked = (it["bookmarked"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                            liked = (it["liked"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                            disliked = (it["disliked"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                        )
                    }

                    User(
                        uid = data["uid"] as String,
                        first_name = data["first_name"] as String,
                        last_name = data["last_name"] as String,
                        fcm_token = data["fcm_token"] as String?,
                        profile_pic = data["profile_pic"] as String?,
                        documents = userDocument,
                        date_of_birth = data["date_of_birth"] as String?,
                        description = data["description"] as String?,
                        upload_count = (data["upload_count"] as? Long)?.toInt() ?: 0,
                    )
                } else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun addLikedDocumentToUser(userId: String, documentId: String) {
        try {
            val userDocRef = usersCollection.document(userId)
            userDocRef.update("documents.liked", FieldValue.arrayUnion(documentId)).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeLikedDocumentFromUser(userId: String, documentId: String) {
        try {
            val userDocRef = usersCollection.document(userId)
            userDocRef.update("documents.liked", FieldValue.arrayRemove(documentId)).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addDislikedDocumentToUser(userId: String, documentId: String) {
        try {
            val userDocRef = usersCollection.document(userId)
            userDocRef.update("documents.disliked", FieldValue.arrayUnion(documentId)).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeDislikedDocumentFromUser(userId: String, documentId: String) {
        try {
            val userDocRef = usersCollection.document(userId)
            userDocRef.update("documents.disliked", FieldValue.arrayRemove(documentId)).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addBookmarkedDocumentToUser(userId: String, documentId: String) {
        try {
            val userDocRef = usersCollection.document(userId)
            userDocRef.update("documents.bookmarked", FieldValue.arrayUnion(documentId)).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeBookmarkedDocumentFromUser(userId: String, documentId: String) {
        try {
            val userDocRef = usersCollection.document(userId)
            userDocRef.update("documents.bookmarked", FieldValue.arrayRemove(documentId)).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addUploadedDocumentToUser(userId: String, documentId: String) {
        try {
            val userDocRef = usersCollection.document(userId)
            userDocRef.update("documents.uploaded", FieldValue.arrayUnion(documentId)).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun increaseUserUpload(userId: String){
        try {
            usersCollection.document(userId)
                .update("upload_count", FieldValue.increment(1))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Updates a specific field in a user document.
     * 
     * @param userId The ID of the user to update
     * @param fieldPath The path to the field to update (e.g., "documents.liked")
     * @param value The new value to set for the field
     * @return Boolean indicating success or failure
     */
    suspend fun updateUserField(userId: String, fieldPath: String, value: Any): Boolean {
        return try {
            // Get a reference to the user document
            val userRef = usersCollection.document(userId)
            
            // Check if the field path contains a dot for nested fields
            if (fieldPath.contains(".")) {
                // For nested fields like "documents.liked", we need to handle differently
                val parts = fieldPath.split(".")
                
                // For the common case of "documents.liked"
                if (parts.size == 2 && parts[0] == "documents") {
                    // Get the current user document
                    val userDoc = userRef.get().await()
                    
                    if (userDoc.exists()) {
                        // Get the current documents map or create a new one
                        val documentsMap = userDoc.get("documents") as? Map<String, Any> ?: hashMapOf()
                        val mutableDocMap = documentsMap.toMutableMap()
                        
                        // Update the specific field in the documents map
                        mutableDocMap[parts[1]] = value
                        
                        // Update the entire documents field with the modified map
                        userRef.update("documents", mutableDocMap).await()
                        true
                    } else {
                        println("DEBUG: User document not found for ID: $userId")
                        false
                    }
                } else {
                    // For other nested fields, use a generic approach
                    userRef.update(fieldPath, value).await()
                    true
                }
            } else {
                // For top-level fields, a simple update is sufficient
                userRef.update(fieldPath, value).await()
                true
            }
        } catch (e: Exception) {
            println("DEBUG: Error updating user field $fieldPath: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}