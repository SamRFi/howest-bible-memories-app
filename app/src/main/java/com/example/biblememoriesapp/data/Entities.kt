package com.example.biblememoriesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class Memory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val verse: String,
    val content: String,
    val timestamp: Long,
    val location: String,
    var isHighlighted: Boolean = false
)




