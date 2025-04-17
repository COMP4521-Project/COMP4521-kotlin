package com.example.comp4521_ustrade.app.screens

sealed class Screens(val screen: String) {
    //top app bar
    object Search : Screens("search")
    object Favorite : Screens("Favorite")
    object ChatRoom : Screens("ChatRoom")
    object Settings : Screens("settings")

    //bottom app bar
    object Home : Screens("home")
    object Download : Screens("download")
    object Notification : Screens("Notification")
    object Profile : Screens("Profile")

    // Settings screens - Account
    object EditProfile : Screens("settings_edit_profile") //TODO: generated
    object EditPassword : Screens("settings_change_password") //TODO: generated
    object NotificationSettings : Screens("settings_notifications") //TODO: generated

    // Settings screens - Resources
    object RedeemGifts : Screens("settings_redeem_gifts") //TODO: Implement
    object Bookmarks : Screens("settings_bookmarks") //TODO: Implement
    object UploadedFiles : Screens("settings_uploaded_files") //TODO: Implement
    object DownloadedFiles : Screens("settings_downloaded_files") //TODO: Implement

    // Settings screens - Support & About
    object Preferences : Screens("settings_preferences") //TODO: generated
    object AboutApp : Screens("settings_about") //TODO: generated

    // Auth
    object Landing : Screens("landing")
    object Login: Screens("Login")
    object Register: Screens("Register")

    // AI
    object AIDetails: Screens("AIDetails")
}
