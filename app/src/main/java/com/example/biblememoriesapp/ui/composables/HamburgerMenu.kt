package com.example.biblememoriesapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biblememoriesapp.data.BibleBook
import com.example.biblememoriesapp.data.BibleBooks
import com.example.biblememoriesapp.viewmodels.BibleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DrawerContent(
    bibleViewModel: BibleViewModel,
    selectedBook: MutableState<BibleBook?>,
    currentPage: Int,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {
    LazyColumn(modifier = Modifier.background(Color.Transparent)) {
        items(BibleBooks.books) { book ->
            Column {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedBook.value = book },
                    color = MaterialTheme.colors.secondary,
                ) {
                    Text(
                        book.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colors.onSecondary
                    )
                }

                if (selectedBook.value == book) {
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        book.chapters.forEach { chapter ->
                            Surface(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clickable {
                                        bibleViewModel.updateChapter(book.name, chapter)
                                        selectedBook.value = book
                                        currentPage.minus(1)
                                        coroutineScope.launch {
                                            scaffoldState.drawerState.close()
                                        }
                                    },
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colors.secondary
                            ) {
                                Text(
                                    text = chapter.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp),
                                    color = MaterialTheme.colors.onSecondary
                                )
                            }
                        }
                    }
                }
            }

            Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f))
        }
    }
}
