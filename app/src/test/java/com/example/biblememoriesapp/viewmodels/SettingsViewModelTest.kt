package com.example.biblememoriesapp.viewmodels


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.biblememoriesapp.data.repositories.SettingsRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var settingsRepository: SettingsRepository

    @Mock
    private lateinit var fontSizeObserver: Observer<Int>

    @Mock
    private lateinit var themeObserver: Observer<String>

    @Mock
    private lateinit var saveLocationWithMemoryObserver: Observer<Boolean>

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = SettingsViewModel(settingsRepository)
    }

    @Test
    fun testFontSize() {
        Mockito.`when`(settingsRepository.fontSize).thenReturn(14)

        viewModel.fontSize.asLiveData().observeForever(fontSizeObserver)
        viewModel.setFontSize(16)

        Mockito.verify(settingsRepository).setFontSize(16)
        Mockito.verify(fontSizeObserver).onChanged(16)
    }

    @Test
    fun testToggleTheme() {
        Mockito.`when`(settingsRepository.theme).thenReturn("Light")

        viewModel.theme.asLiveData().observeForever(themeObserver)

        val initialTheme = viewModel.theme.value
        val expectedNewTheme = if (initialTheme == "Light") "Dark" else "Light"

        viewModel.toggleTheme()

        Mockito.verify(settingsRepository).setTheme(expectedNewTheme)
        Mockito.verify(themeObserver).onChanged(expectedNewTheme)
    }


    @Test
    fun testSaveLocationWithMemory() {
        Mockito.`when`(settingsRepository.saveLocationWithMemory).thenReturn(false)

        viewModel.saveLocationWithMemory.asLiveData().observeForever(saveLocationWithMemoryObserver)
        viewModel.setSaveLocationWithMemory(true)

        Mockito.verify(settingsRepository).setSaveLocationWithMemory(true)
        Mockito.verify(saveLocationWithMemoryObserver).onChanged(true)
    }
}
