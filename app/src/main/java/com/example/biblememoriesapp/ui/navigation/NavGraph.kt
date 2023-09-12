package com.example.biblememoriesapp.ui.navigation

sealed class Screen(val route: String) {
    object BibleInstance1 : Screen("bibleInstance1")
    object BibleInstance2 : Screen("bibleInstance2")
    object Memories : Screen("memories")
    object Settings : Screen("settings")
}

