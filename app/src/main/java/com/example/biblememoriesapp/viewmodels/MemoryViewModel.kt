package com.example.biblememoriesapp.viewmodels

import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.biblememoriesapp.data.Memory
import com.example.biblememoriesapp.data.repositories.MemoryRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.Locale
import android.Manifest
import com.example.biblememoriesapp.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.io.IOException

class MemoryViewModel(
    private val memoryRepository: MemoryRepository,
    private val application: Application,
) : ViewModel() {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var settingsViewModel: SettingsViewModel

    private val _memories = MutableStateFlow<List<Memory>>(emptyList())
    val memories: StateFlow<List<Memory>> = _memories

    val newLocation = mutableStateOf("")

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    init {
        loadMemories()
    }

    fun fetchLocation() {
        viewModelScope.launch {
            if (ActivityCompat.checkSelfPermission(
                    application,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    val geocoder = Geocoder(application, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )
                        if (!addresses.isNullOrEmpty()) {
                            newLocation.value = addresses[0]?.locality ?: "Unknown"
                        } else {
                            newLocation.value = "Unknown"
                        }
                    } catch (e: IOException) {
                        // handle exception
                        newLocation.value = "Unknown"
                    }
                } else {
                    // Location is null
                    newLocation.value = "Unknown"
                }
            } else {
                // Permission is denied
            }
        }
    }


    fun loadMemories() = viewModelScope.launch {
        _memories.value = memoryRepository.getAllMemories()
    }

    fun insertMemory(verse: String, content: String) = viewModelScope.launch {

        settingsRepository = SettingsRepository(application)
        settingsViewModel = SettingsViewModel(settingsRepository)

        val currentTimestamp = System.currentTimeMillis()
        val saveLocationWithMemory = settingsRepository.saveLocationWithMemory
        val memory = Memory(
            verse = verse,
            content = content,
            timestamp = currentTimestamp,
            location = if (saveLocationWithMemory) newLocation.value else "On Earth"
        )
        memoryRepository.insertMemory(memory)
        loadMemories()
    }


    fun updateMemory(memory: Memory) = viewModelScope.launch {
        memoryRepository.updateMemory(memory)
        loadMemories()
    }

    fun deleteMemory(id: Long) = viewModelScope.launch {
        memoryRepository.deleteMemory(id)
        loadMemories()
    }

}


class MemoryViewModelFactory(
    private val application: Application,
    private val memoryRepository: MemoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoryViewModel::class.java)) {
            return MemoryViewModel(memoryRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


