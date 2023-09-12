package com.example.biblememoriesapp.data


import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var fontSize: Int
        get() = sharedPreferences.getInt(FONT_SIZE_KEY, DEFAULT_FONT_SIZE)
        set(value) = sharedPreferences.edit().putInt(FONT_SIZE_KEY, value).apply()

    var theme: String
        get() = sharedPreferences.getString(THEME_KEY, DEFAULT_THEME) ?: DEFAULT_THEME
        set(value) = sharedPreferences.edit().putString(THEME_KEY, value).apply()

    var saveLocationWithMemory: Boolean
        get() = sharedPreferences.getBoolean(LOCATION_KEY, DEFAULT_SAVE_LOCATION)
        set(value) = sharedPreferences.edit().putBoolean(LOCATION_KEY, value).apply()

    companion object {
        private const val PREFERENCES_NAME = "user_settings"
        private const val FONT_SIZE_KEY = "font_size"
        private const val THEME_KEY = "theme"
        private const val DEFAULT_FONT_SIZE = 16
        private const val DEFAULT_THEME = "Light"
        private const val LOCATION_KEY = "save_location_with_memory"
        private const val DEFAULT_SAVE_LOCATION = true
    }
}
