package com.example.biblememoriesapp.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.biblememoriesapp.R
import com.example.biblememoriesapp.data.repositories.MemoryRepository
import com.example.biblememoriesapp.ui.navigation.Screen
import com.example.biblememoriesapp.viewmodels.BibleViewModel
import com.example.biblememoriesapp.viewmodels.BibleViewModelFactory
import com.example.biblememoriesapp.viewmodels.MainViewModel
import com.example.biblememoriesapp.viewmodels.MemoryViewModel
import com.example.biblememoriesapp.viewmodels.MemoryViewModelFactory
import com.example.biblememoriesapp.viewmodels.SettingsViewModel
import com.example.biblememoriesapp.viewmodels.SettingsViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Navigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val items = listOf(
        NavigationItem(Screen.BibleInstance1, Icons.Default.Home, stringResource(R.string.bible_1)),
        NavigationItem(Screen.BibleInstance2, Icons.Default.Home, stringResource(R.string.bible_2)),
        NavigationItem(Screen.Memories, Icons.Filled.Favorite, stringResource(R.string.memories)),
        NavigationItem(Screen.Settings, Icons.Filled.Settings, stringResource(R.string.settings))
    )

    // View models
    val bibleViewModel1: BibleViewModel = viewModel(
        key = "bibleViewModel1",
        factory = BibleViewModelFactory(
            application = mainViewModel.getApplication(),
            book = "Genesis",
            chapter = 1,
            key = "bibleViewModel1"
        )
    )
    val bibleViewModel2: BibleViewModel = viewModel(
        key = "bibleViewModel2",
        factory = BibleViewModelFactory(
            application = mainViewModel.getApplication(),
            book = "Exodus",
            chapter = 1,
            key = "bibleViewModel2"
        )
    )
    val memoryViewModel: MemoryViewModel = viewModel(
        factory = MemoryViewModelFactory(
            application = mainViewModel.getApplication(),
            memoryRepository = MemoryRepository(mainViewModel.memoryDao)
        )
    )
    val settingsViewModelFactory = SettingsViewModelFactory(mainViewModel.settingsRepository)
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

    Scaffold(
        bottomBar = { AppBottomNavigation(items = items, navController = navController)  }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.BibleInstance1.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.BibleInstance1.route) {
                BibleScreenInstance1(bibleViewModel1, settingsViewModel)
            }
            composable(Screen.BibleInstance2.route) {
                BibleScreenInstance2(bibleViewModel2, settingsViewModel)
            }
            composable(Screen.Memories.route) { MemoryScreen(memoryViewModel) }
            composable(Screen.Settings.route) { SettingsScreen(settingsViewModel= settingsViewModel)}
        }
    }
}

data class NavigationItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

@Composable
fun AppBottomNavigation(
    items: List<NavigationItem>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(modifier = modifier) {
        items.forEach { navigationItem ->
            BottomNavigationItem(
                icon = { Icon(navigationItem.icon, contentDescription = navigationItem.label) },
                label = { Text(navigationItem.label) },
                selected = currentRoute == navigationItem.screen.route,
                onClick = {
                    if (currentRoute != navigationItem.screen.route) {
                        navController.navigate(navigationItem.screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.testTag(navigationItem.screen.route)
            )
        }
    }
}






