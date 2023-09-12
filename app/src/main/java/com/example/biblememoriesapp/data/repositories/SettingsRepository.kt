package com.example.biblememoriesapp.data.repositories

import android.app.Application
import com.example.biblememoriesapp.data.SettingsManager

class SettingsRepository(private val application: Application) {

    private val settingsManager = SettingsManager(application)

    val fontSize get() = settingsManager.fontSize
    val theme get() = settingsManager.theme
    val saveLocationWithMemory get() = settingsManager.saveLocationWithMemory

    fun setFontSize(size: Int) {
        settingsManager.fontSize = size
    }

    fun setTheme(theme: String) {
        settingsManager.theme = theme
    }

    fun setSaveLocationWithMemory(save: Boolean) {
        settingsManager.saveLocationWithMemory = save
    }
}
