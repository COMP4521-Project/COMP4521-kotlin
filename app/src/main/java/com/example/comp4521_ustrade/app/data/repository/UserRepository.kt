package com.example.comp4521_ustrade.app.data.repository

import com.example.comp4521_ustrade.app.data.dao.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun addUser(user: User) {
        try {
            usersCollection.document(user.uid).set(user).await()
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
                document.toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}