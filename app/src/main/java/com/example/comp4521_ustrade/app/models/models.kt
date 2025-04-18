package com.example.comp4521_ustrade.app.models

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

//Home Page
data class CourseCardItem(
    @DrawableRes val thumbnail: Int,
    val courseTitle: String,
    val courseYear: String,
    val courseSemester: String,

)

//bottom navigation
data class NavItem(
    var label: String,
    var icon : ImageVector,
    var badgecount: Int = 0,
)


//navigation drawer
data class DrawerItem(
    val icon: ImageVector,
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

//prize
data class Prize(
    @DrawableRes val icon: Int,
)