package com.example.biblememoriesapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.biblememoriesapp.data.BibleMemoriesDatabase
import com.example.biblememoriesapp.data.MemoryDao
import com.example.biblememoriesapp.data.repositories.SettingsRepository


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database: BibleMemoriesDatabase = BibleMemoriesDatabase.getInstance(application)
    val memoryDao: MemoryDao = database.memoryDao()
    val settingsRepository: SettingsRepository = SettingsRepository(application)


}
