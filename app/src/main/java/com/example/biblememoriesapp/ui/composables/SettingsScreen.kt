package com.example.biblememoriesapp.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.biblememoriesapp.R
import com.example.biblememoriesapp.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val fontSize by settingsViewModel.fontSize.collectAsState()
    val theme by settingsViewModel.theme.collectAsState()
    val saveLocationWithMemory by settingsViewModel.saveLocationWithMemory.collectAsState()
    val appIconPainter = painterResource(id = R.mipmap.settings_image)

    Box(modifier = Modifier.fillMaxSize().testTag("SettingsScreen"), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppIcon(appIconPainter)
            Spacer(modifier = Modifier.height(50.dp))
            FontSizeSection(fontSize, settingsViewModel::setFontSize)
            Spacer(modifier = Modifier.height(50.dp))
            ThemeSection(theme, settingsViewModel::toggleTheme)
            Spacer(modifier = Modifier.height(50.dp))
            SaveLocationSection(saveLocationWithMemory, settingsViewModel::setSaveLocationWithMemory)
        }
    }
}

@Composable
fun AppIcon(painter: Painter) {
    Image(painter = painter, contentDescription = "App Icon")
}

@Composable
fun FontSizeSection(fontSize: Int, onFontSizeChange: (Int) -> Unit) {
    Text("${stringResource(R.string.font_size)}: $fontSize")
    Slider(value = fontSize.toFloat(), onValueChange = { newValue ->
        onFontSizeChange(newValue.toInt())
    }, valueRange = 8f..24f)
}

@Composable
fun ThemeSection(theme: String, onToggleTheme: () -> Unit) {
    Text("Theme: $theme")
    Button(onClick = onToggleTheme) {
        Text(if (theme == "Light") stringResource(R.string.switch_to_dark_theme) else stringResource(
                    R.string.switch_to_light_theme)
                )
    }
}

@Composable
fun SaveLocationSection(saveLocationWithMemory: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Text(stringResource(R.string.save_location_with_memory))
    Switch(checked = saveLocationWithMemory, onCheckedChange = onCheckedChange)
}

