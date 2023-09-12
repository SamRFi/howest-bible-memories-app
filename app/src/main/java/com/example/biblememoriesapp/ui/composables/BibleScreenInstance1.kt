package com.example.biblememoriesapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.biblememoriesapp.viewmodels.BibleViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableIntStateOf
import com.example.biblememoriesapp.data.BibleBook
import com.example.biblememoriesapp.ui.theme.DEFAULT_FONT_SIZE
import com.example.biblememoriesapp.viewmodels.SettingsViewModel

@Composable
fun BibleScreenInstance1(bibleViewModel: BibleViewModel, settingsViewModel: SettingsViewModel) {
    val scaffoldState = rememberScaffoldState()
    val selectedBook = remember { mutableStateOf<BibleBook?>(null) }
    val currentPage = remember { mutableIntStateOf(1) }
    val coroutineScope = rememberCoroutineScope()
    val currentBook by bibleViewModel.selectedBook.collectAsState(initial = "Book")
    val currentChapter by bibleViewModel.selectedChapter.collectAsState(initial = 1)

    Scaffold (
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerContent(bibleViewModel, selectedBook, currentChapter, scaffoldState, coroutineScope)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "$currentBook ${currentChapter}"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Local Menu")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        bibleViewModel.previousChapter()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Previous Chapter")
                    }
                    IconButton(onClick = {
                        bibleViewModel.nextChapter()
                    }) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "Next Chapter")
                    }

                }
            )
        }
    ) { innerPadding ->

        val chapterText by bibleViewModel.chapterTextFlow.collectAsState(initial = "Loading...")
        val scrollState = rememberScrollState()
        val fontSize = settingsViewModel.fontSize.collectAsState().value ?: DEFAULT_FONT_SIZE


        Column(modifier = Modifier.verticalScroll(scrollState).padding(innerPadding)) {
            BibleTextStyle(
                fontSize = fontSize,
                text = chapterText,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

