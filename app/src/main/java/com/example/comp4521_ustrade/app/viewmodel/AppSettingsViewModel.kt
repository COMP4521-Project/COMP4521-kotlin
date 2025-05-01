package com.example.comp4521_ustrade.app.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//toggle the chatbot on and off
class AppSettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    private val _isChatbotEnabled = MutableStateFlow(prefs.getBoolean("is_chatbot_enabled", true))
    val isChatbotEnabled: StateFlow<Boolean> = _isChatbotEnabled

    fun setChatbotEnabled(enabled: Boolean) {
        _isChatbotEnabled.value = enabled
        prefs.edit().putBoolean("is_chatbot_enabled", enabled).apply()
    }
}