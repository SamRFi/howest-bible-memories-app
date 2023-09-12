package com.example.biblememoriesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MemoryDao {
    @Insert
    suspend fun insert(memory: Memory): Long

    @Update
    suspend fun update(memory: Memory)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Query("SELECT * FROM memories ORDER BY timestamp DESC")
    suspend fun getAllMemories(): List<Memory>

    @Query("SELECT * FROM memories WHERE timestamp BETWEEN :start AND :end")
    suspend fun getMemoryFromTimeAgo(start: Long, end: Long): List<Memory>

    @Query("UPDATE memories SET isHighlighted = 0")
    suspend fun unHighlightAllMemories()

}

