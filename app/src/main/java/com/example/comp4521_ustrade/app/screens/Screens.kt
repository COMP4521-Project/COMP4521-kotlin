package com.example.comp4521_ustrade.app.screens

sealed class Screens(val screen: String) {
    //top app bar
    object Search : Screens("search")
    object Favorite : Screens("Favorite")
    object ChatRoom : Screens("ChatRoom")


    //bottom app bar
    object Home : Screens("home")
    object Download : Screens("download")
    object Notification : Screens("Notification")
    object Profile : Screens("Profile")
}
