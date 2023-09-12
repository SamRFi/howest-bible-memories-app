package com.example.biblememoriesapp.data.repositories

import com.example.biblememoriesapp.data.api.ApiClient
import com.example.biblememoriesapp.services.BibleApiService
import javax.inject.Inject

class BibleRepository @Inject constructor(private val service: BibleApiService) {

    suspend fun getChapter(book: String, chapter: Int) = service.getChapter(book, chapter)

}