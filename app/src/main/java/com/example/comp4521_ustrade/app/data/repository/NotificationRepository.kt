package com.example.comp4521_ustrade.app.data.repository

import com.example.comp4521_ustrade.app.data.dao.UserNotification
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class NotificationRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val notificationsCollection = firestore.collection("notifications")

    suspend fun addNotification(targetUserId: String, notification: UserNotification) {
        try {
            // Generate ID if not provided
            val notificationWithId = if (notification.id.isEmpty()) {
                notification.copy(id = UUID.randomUUID().toString())
            } else {
                notification
            }
            
            val currentTimestamp = System.currentTimeMillis()
            
            // Create notification map
            val notificationMap = hashMapOf(
                "id" to notificationWithId.id,
                "from_user_id" to notificationWithId.from_user_id,
                "from_user_name" to notificationWithId.from_user_name,
                "from_user_propic" to notificationWithId.from_user_propic,
                "updated_at" to currentTimestamp,
                "message" to notificationWithId.message,
                "read" to notificationWithId.read,
                "related_document_id" to notificationWithId.related_document_id
            )
            
            
            // Get the document reference
            val docRef = notificationsCollection.document(targetUserId)
            
            // Check if the document exists
            val docSnapshot = docRef.get().await()
            
            if (!docSnapshot.exists()) {
                // Create new document if it doesn't exist
                docRef.set(hashMapOf(
                    "userId" to targetUserId,
                    "updated_at" to currentTimestamp,
                    "notifications" to listOf(notificationMap)
                )).await()
            } else {
                // Update existing document
                val batch = firestore.batch()
                
                // Add new notification to the array
                batch.update(docRef, "notifications", FieldValue.arrayUnion(notificationMap))
                
                // Update the timestamp
                batch.update(docRef, "updated_at", currentTimestamp)
                
                batch.commit().await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getUserNotifications(userId: String): List<UserNotification> {
        return try {
            val document = notificationsCollection.document(userId).get().await()
            
            if (document.exists()) {
                val notificationsArray = document.get("notifications") as? List<Map<String, Any>> ?: emptyList()
                
                // Convert to UserNotification objects
                notificationsArray.mapNotNull { data ->
                    try {
                        UserNotification(
                            id = data["id"] as String,
                            from_user_id = data["from_user_id"] as String,
                            from_user_name = data["from_user_name"] as String,
                            from_user_propic = data["from_user_propic"] as? String,
                            updated_at = data["updated_at"] as Long,
                            message = data["message"] as String,
                            read = data["read"] as Boolean,
                            related_document_id = data["related_document_id"] as? String
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }.sortedByDescending { it.updated_at }  // Sort by timestamp (newest first)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun markNotificationAsRead(targetUserId: String, notificationId: String) {
        try {
            val docRef = notificationsCollection.document(targetUserId)
            val document = docRef.get().await()
            
            if (document.exists()) {
                val notificationsArray = document.get("notifications") as? List<Map<String, Any>> ?: return
                
                // Find the notification and update its read status
                val updatedNotifications = notificationsArray.map { notificationData ->
                    if (notificationData["id"] == notificationId) {
                        // Create a copy with read = true
                        val updatedNotification = notificationData.toMutableMap()
                        updatedNotification["read"] = true
                        updatedNotification
                    } else {
                        notificationData
                    }
                }
                
                // Update the notifications array and the updated_at timestamp
                docRef.update(
                    "notifications", updatedNotifications,
                ).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteNotification(targetUserId: String, notificationId: String) {
        try {
            val docRef = notificationsCollection.document(targetUserId)
            val document = docRef.get().await()
            
            if (document.exists()) {
                val notificationsArray = document.get("notifications") as? List<Map<String, Any>> ?: return
                
                // Filter out the notification to delete
                val updatedNotifications = notificationsArray.filter { it["id"] != notificationId }
                
                // Update the notifications array and the updated_at timestamp
                docRef.update(
                    "notifications", updatedNotifications,
                ).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}