package com.example.biblememoriesapp.ui.composables

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.biblememoriesapp.data.Memory
import com.example.biblememoriesapp.ui.theme.Shapes

@Composable
fun MemoryCard(memory: Memory, onDelete: (Long) -> Unit, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = MaterialTheme.colors.surface, shape = Shapes.medium)
            .padding(8.dp)
    ) {
        Column {
            Text(
                memory.verse,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatTimestamp(memory.timestamp))

                Text(
                    text = memory.location,
                    style = MaterialTheme.typography.subtitle1,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.clickable { openLocation(context, memory.location) }
                )

            }
            Text(memory.content)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ShareMemory(memory, context)
                DeleteMemory(memory.id, onDelete)
            }
        }
    }
}

@Composable
fun ShareMemory(memory: Memory, context: Context) {
    IconButton(onClick = {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Memory: ${memory.verse}\nThoughts: ${memory.content}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }) {
        Icon(Icons.Filled.Share, contentDescription = "Share Memory")
    }
}

@Composable
fun DeleteMemory(memoryId: Long, onDelete: (Long) -> Unit) {
    IconButton(onClick = { onDelete(memoryId) }) {
        Icon(Icons.Filled.Delete, contentDescription = "Delete memory")
    }
}

fun openLocation(context: Context, location: String) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=$location")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$location"))
        context.startActivity(browserIntent)
    }
}
