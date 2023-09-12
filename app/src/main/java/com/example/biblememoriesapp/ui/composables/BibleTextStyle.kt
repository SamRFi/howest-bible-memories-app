package com.example.biblememoriesapp.ui.composables


import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun BibleTextStyle(fontSize: Int, text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = fontSize.sp,
        modifier = modifier
    )
}
