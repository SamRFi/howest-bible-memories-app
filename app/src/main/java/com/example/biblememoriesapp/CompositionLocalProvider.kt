package com.example.biblememoriesapp

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.biblememoriesapp.data.MemoryDao
import com.example.biblememoriesapp.data.repositories.SettingsRepository

val LocalMemoryDao = staticCompositionLocalOf<MemoryDao> {
    error("No MemoryDao provided")
}

val LocalSettingsRepository = staticCompositionLocalOf<SettingsRepository> {
    error("No SettingsRepository provided")
}
