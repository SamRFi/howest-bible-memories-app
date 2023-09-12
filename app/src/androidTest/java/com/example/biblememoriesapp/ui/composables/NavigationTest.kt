package com.example.biblememoriesapp.ui.composables

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblememoriesapp.viewmodels.MainViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    private val mainViewModel = MainViewModel(ApplicationProvider.getApplicationContext())

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bottomNavigationItems_displayed() {
        composeTestRule.setContent {


            Navigation(mainViewModel = mainViewModel)
        }

        composeTestRule.onNodeWithTag("bibleInstance1").assertExists()
        composeTestRule.onNodeWithTag("bibleInstance2").assertExists()
        composeTestRule.onNodeWithTag("memories").assertExists()
        composeTestRule.onNodeWithTag("settings").assertExists()
    }

    @Test
    fun bottomNavigationItems_navigateToCorrectScreens() {
        composeTestRule.setContent {
            Navigation(mainViewModel = mainViewModel)
        }

        composeTestRule.onNodeWithTag("memories").performClick()

        composeTestRule.onNodeWithTag("MemoryScreen").assertExists()

        composeTestRule.onNodeWithTag("settings").performClick()

        composeTestRule.onNodeWithTag("SettingsScreen").assertExists()
    }
}
