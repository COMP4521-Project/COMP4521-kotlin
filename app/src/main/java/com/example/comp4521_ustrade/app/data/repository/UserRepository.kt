package com.example.comp4521_ustrade.app.data.repository

import androidx.core.net.toUri
import com.example.comp4521_ustrade.app.data.dao.User
import com.example.comp4521_ustrade.app.data.dao.UserDocument
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
                "documents" to user.documents
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
                    User(
                        uid = data["uid"] as String,
                        first_name = data["first_name"] as String,
                        last_name = data["last_name"] as String,
                        fcm_token = data["fcm_token"] as String?,
                        profile_pic = data["profile_pic"] as String?,
                        documents = data["documents"] as UserDocument?
                    )
                } else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}