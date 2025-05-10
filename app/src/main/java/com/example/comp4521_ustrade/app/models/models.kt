package com.example.comp4521_ustrade.app.models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

//Home Page
data class CourseCardItem(
    val thumbnailUrl: String?,
    val course: String,
    val courseTitle: String,
    val courseYear: String,
    val courseSemester: String,
    val documentId: String

)

//bottom navigation
data class NavItem(
    var label: String,
    var icon : ImageVector,
    var badgecount: Int = 0,
)


//navigation drawer (depreciated)
data class DrawerItem(
//    val icon: ImageVector,
    val label: String,
    val secondary_label: String
)


//Profile Page
data class ProfileCardData(
    @DrawableRes val profilePicture: Int,
    val name: String,
    val upload_count : Int,
    val download_count : Int,
)



//shortcuts on the Profile screen
data class ShortCutCardItem(
    @DrawableRes val icon: Int,
    val title: String,
)

data class Document(
    val id: String,
    val title: String,
    val subtitle: String,
    val term: String,
    val type: String,
    val imageUrl: String
)

data class NotificationData(
    val userAvatar: String,
    val userName: String,
    val message: String,
    val time: String,
    val isRead: Boolean
)

data class DisplayFieldItem(
    val title: String,
    val value: String,
    val onClick: (() -> Unit)? = null,  // Optional click handler
)

data class DisplayOnlyFieldItem(
    val title: String,
    val value: String
)

data class FunctionCardData(
    val icon: ImageVector,
    val title: String
)

//prize
data class Prize(
    @DrawableRes val icon: Int,
)

enum class Course {
    COMP4521,
    COMP2011,
    COMP2012
}