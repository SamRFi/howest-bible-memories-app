package com.example.biblememoriesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.biblememoriesapp.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _fontSize = MutableStateFlow(settingsRepository.fontSize)
    val fontSize: StateFlow<Int> = _fontSize

    private val _theme = MutableStateFlow(settingsRepository.theme)
    val theme: StateFlow<String> = _theme

    private val _saveLocationWithMemory = MutableStateFlow(settingsRepository.saveLocationWithMemory)
    val saveLocationWithMemory: StateFlow<Boolean> = _saveLocationWithMemory

    init {
        _fontSize.value = settingsRepository.fontSize
        _theme.value = settingsRepository.theme
        _saveLocationWithMemory.value = settingsRepository.saveLocationWithMemory
    }

    fun setFontSize(size: Int) {
        settingsRepository.setFontSize(size)
        _fontSize.value = size
    }

    fun toggleTheme() {
        val newTheme = if (_theme.value == "Light") "Dark" else "Light"
        settingsRepository.setTheme(newTheme)
        _theme.value = newTheme
    }


    fun setSaveLocationWithMemory(save: Boolean) {
        settingsRepository.setSaveLocationWithMemory(save)
        _saveLocationWithMemory.value = save
    }
}


class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}










