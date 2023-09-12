package com.example.biblememoriesapp.services

import com.example.biblememoriesapp.data.api.BibleVerseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BibleApiService {
    @GET("{book}{chapter}?translation=kjv")
    suspend fun getChapter(
        @Path("book") book: String,
        @Path("chapter") chapter: Int
    ): Response<BibleVerseResponse>
}
