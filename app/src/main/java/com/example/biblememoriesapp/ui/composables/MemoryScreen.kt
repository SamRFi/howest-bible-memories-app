package com.example.biblememoriesapp.ui.composables

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biblememoriesapp.R
import com.example.biblememoriesapp.viewmodels.MemoryViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.*
import com.example.biblememoriesapp.ui.theme.Shapes

@ExperimentalPermissionsApi
@Composable
fun MemoryScreen(memoryViewModel: MemoryViewModel = viewModel()) {
    val context = LocalContext.current
    val memories by memoryViewModel.memories.collectAsState(initial = emptyList())

    val highlightedMemory = memories.filter { it.isHighlighted }
    val nonHighlightedMemories = memories.filter { !it.isHighlighted }

    LazyColumn(modifier = Modifier.testTag("MemoryScreen")) {
        highlightedMemory.forEach { memory ->
            item {
                Text(
                    stringResource(R.string.exactly_1_month_ago_you_had_this_encounter),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )

                MemoryCard(memory, onDelete = memoryViewModel::deleteMemory, context)
            }
        }

        item {
            AddMemoryInput(memoryViewModel)
        }

        items(nonHighlightedMemories) { memory ->
            MemoryCard(memory, onDelete = memoryViewModel::deleteMemory, context)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddMemoryInput(memoryViewModel: MemoryViewModel) {
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    var newVerse by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(color = MaterialTheme.colors.surface, shape = Shapes.medium)
            .padding(8.dp)
    ) {
        Column {
            OutlinedTextField(
                value = newVerse,
                onValueChange = { newVerse = it },
                label = { Text(stringResource(R.string.enter_verse)) },
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.which_verse_do_you_want_to_remind)) }
            )

            OutlinedTextField(
                value = newContent,
                onValueChange = { newContent = it },
                label = { Text(stringResource(R.string.what_s_on_your_mind)) },
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    if (locationPermissionState.status == PermissionStatus.Granted) {
                        memoryViewModel.fetchLocation()
                        memoryViewModel.insertMemory(
                            verse = newVerse,
                            content = newContent
                        )
                    } else {
                        locationPermissionState.launchPermissionRequest()
                    }
                }, shape = Shapes.large) {
                    Text(if (locationPermissionState.status == PermissionStatus.Granted) stringResource(
                                            R.string.add_memory) else stringResource(R.string.request_permission)
                                                                )
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}
