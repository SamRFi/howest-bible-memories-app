package com.example.biblememoriesapp.ui.theme

import android.app.Application
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biblememoriesapp.data.repositories.SettingsRepository
import com.example.biblememoriesapp.viewmodels.SettingsViewModel
import com.example.biblememoriesapp.viewmodels.SettingsViewModelFactory

const val DEFAULT_FONT_SIZE = 16

private val DarkColorPalette = darkColors(
    primary = LightBlue,
    primaryVariant = VeryDarkGray,
    secondary = LightGray,
    background = Color.Black,
    surface = VeryDarkGray,
    onPrimary = OffWhite,
    onSecondary = Color.Black,
    onBackground = LightGray,
    onSurface = OffWhite,
)

private val LightColorPalette = lightColors(
    primary = LightBlue,
    primaryVariant = DarkBlue,
    secondary = DarkGray,
    background = OffWhite,
    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = OffWhite,
    onBackground = Color.Black,
    onSurface = Color.Black,
)


@Composable
fun BibleMemoriesAppTheme(
    content: @Composable () -> Unit
) {
    val settingsRepository = SettingsRepository(LocalContext.current.applicationContext as Application)
    val settingsViewModelFactory = SettingsViewModelFactory(settingsRepository)
    val settingsViewModel: SettingsViewModel = viewModel(factory = settingsViewModelFactory)

    val currentTheme by settingsViewModel.theme.collectAsState()
    val darkTheme = currentTheme.let { it == "Dark" } ?: isSystemInDarkTheme()

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val typography = Typography(
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal
        ),
        button = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500
        ),
        caption = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal
        )
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}

