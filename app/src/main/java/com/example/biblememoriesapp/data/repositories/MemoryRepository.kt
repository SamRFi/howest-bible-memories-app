package com.example.biblememoriesapp.data.repositories

import com.example.biblememoriesapp.data.Memory
import com.example.biblememoriesapp.data.MemoryDao


class MemoryRepository(private val memoryDao: MemoryDao) {

    suspend fun insertMemory(memory: Memory) = memoryDao.insert(memory)

    suspend fun updateMemory(memory: Memory) = memoryDao.update(memory)

    suspend fun deleteMemory(id: Long) = memoryDao.delete(id)

    suspend fun getAllMemories(): List<Memory> = memoryDao.getAllMemories()

    suspend fun getMemoriesFromTimeAgo(start: Long, end: Long): List<Memory> = memoryDao.getMemoryFromTimeAgo(start, end)

    suspend fun unHighlightAllMemories() = memoryDao.unHighlightAllMemories()

}
