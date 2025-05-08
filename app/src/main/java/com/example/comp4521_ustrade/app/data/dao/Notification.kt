package com.example.comp4521_ustrade.app.data.dao

import android.net.Uri

// Main notification document structure
data class UserNotifications(
    val targetUserId: String,  // Document ID
    val userId: String,        // Same as targetUserId for clarity
    val updated_at: Long,      // Last update timestamp
    val notifications: List<UserNotification> = emptyList()
)

// Individual notification item structure
data class UserNotification(
    val id: String,
    val from_user_id: String,
    val from_user_name: String,
    val from_user_propic: String?,
    val updated_at: Long,
    val message: String,
    val read: Boolean = false,
    val related_document_id: String? = null
)