package com.example.comp4521_ustrade.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comp4521_ustrade.app.screens.Screens


//bottom nav bar
class NavViewModel : ViewModel() {
    private val _selectedScreen = MutableLiveData<Screens>(Screens.Home) // Default to Home screen
    val selectedScreen: LiveData<Screens> = _selectedScreen

    fun setSelectedScreen(screen: Screens) {
        _selectedScreen.value = screen
    }
}