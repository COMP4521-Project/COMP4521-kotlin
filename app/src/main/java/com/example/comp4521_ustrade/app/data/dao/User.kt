package com.example.comp4521_ustrade.app.data.dao


data class User (
    val uid: String,
    val fcm_token: String? = null,
    val first_name: String,
    val last_name: String,
    val profile_pic: String? = null,
    val documents: UserDocument? = null,
    val date_of_birth: String? = null,
    val description: String? = null
)

class UserDocument (
    val uploaded: List<String>,
    val downloaded: List<Document>,
    val bookmarked: List<String>,
    val liked: List<String>,
    val disliked: List<String>,
)