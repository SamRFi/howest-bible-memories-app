package com.example.biblememoriesapp.data.api

data class BibleVerseResponse(
    val reference: String,
    val verses: List<Verse>,
    val text: String,
    val translation_id: String,
    val translation_name: String,
    val translation_note: String
)

data class Verse(
    val book_id: String,
    val book_name: String,
    val chapter: Int,
    val verse: Int,
    val text: String
)

