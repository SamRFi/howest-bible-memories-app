package com.example.biblememoriesapp.services

import com.example.biblememoriesapp.data.api.BibleVerseResponse
import com.example.biblememoriesapp.data.api.Verse
import com.example.biblememoriesapp.data.repositories.BibleRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

class BibleRepositoryTest {

    private lateinit var service: BibleApiService
    private lateinit var repository: BibleRepository

    @Before
    fun setUp() {
        service = mock(BibleApiService::class.java)
        repository = BibleRepository(service)
    }


    @Test
    fun getChapterReturnsExpectedResult() = runBlocking {
        // Arrange
        val verse = Verse(
            "JHN",
            "John",
            3,
            16,
            "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.\n"
        )
        val bibleVerseResponse = BibleVerseResponse(
            "John 3",
            listOf(verse),
            "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.\n",
            "kjv",
            "King James Version",
            "Public Domain"
        )
        val expected = Response.success(bibleVerseResponse)

        `when`(service.getChapter("John", 3)).thenReturn(expected)

        // Act
        val result = repository.getChapter("John", 3)

        // Assert
        assertEquals(expected, result)
    }
}
