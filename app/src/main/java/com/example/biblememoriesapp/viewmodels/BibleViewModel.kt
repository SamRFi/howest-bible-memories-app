package com.example.biblememoriesapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.biblememoriesapp.data.BibleBook
import com.example.biblememoriesapp.data.BibleBooks
import com.example.biblememoriesapp.data.api.ApiClient
import com.example.biblememoriesapp.data.repositories.BibleRepository
import com.example.biblememoriesapp.services.BibleApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BibleViewModel(private val bibleRepository: BibleRepository, private var _book: String, private var _chapter: Int) : ViewModel() {

    private val currentBibleBook: BibleBook get() = BibleBooks.books.find { it.name == _selectedBook.value } ?: throw IllegalArgumentException("Unknown book: ${_selectedBook.value}")

    private val _selectedBook = MutableStateFlow(_book)
    val selectedBook: StateFlow<String> = _selectedBook

    private val _selectedChapter = MutableStateFlow(_chapter)
    val selectedChapter: StateFlow<Int> = _selectedChapter

    private val _chapterText = MutableStateFlow<String>("Loading...")
    val chapterTextFlow: StateFlow<String> = _chapterText

    init {
        fetchChapterText()
    }

    private fun fetchChapterText() = viewModelScope.launch {
        try {
            val response = bibleRepository.getChapter(_selectedBook.value, _selectedChapter.value)
            if (response.isSuccessful) {
                _chapterText.value = response.body()?.text.toString()
            } else {
                Log.e("BibleViewModel", "Error: ${response.errorBody()}")
            }
        } catch (e: Exception) {
            Log.e("BibleViewModel", "Exception: $e")
        }
    }

    fun nextChapter() {
        if (_selectedChapter.value < currentBibleBook.chapters.size) {
            _selectedChapter.value = _selectedChapter.value + 1
            fetchChapterText()
        }
    }

    fun previousChapter() {
        if (_selectedChapter.value > 1) {
            _selectedChapter.value = _selectedChapter.value - 1
            fetchChapterText()
        }
    }

    fun updateChapter(book: String, chapter: Int) {
        _selectedBook.value = book
        _selectedChapter.value = chapter.coerceIn(1, currentBibleBook.chapters.size)
        fetchChapterText()
    }

    fun reset() {
        _selectedBook.value = _book
        _selectedChapter.value = _chapter
        fetchChapterText()
    }

}

class BibleViewModelFactory(
    private val application: Application,
    private val book: String,
    private val chapter: Int,
    private val key: String,
    private val service: BibleApiService = ApiClient.retrofit.create(BibleApiService::class.java)
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BibleViewModel::class.java)) {
            val bibleRepository = BibleRepository(service)
            return BibleViewModel(bibleRepository, book, chapter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
