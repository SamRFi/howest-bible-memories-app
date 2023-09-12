package com.example.biblememoriesapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MemoryDaoTest {
    private lateinit var database: BibleMemoriesDatabase
    private lateinit var memoryDao: MemoryDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, BibleMemoriesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        memoryDao = database.memoryDao()
    }

    @After
    fun tearDown() {
        if (this::database.isInitialized) {
            database.close()
        }
    }

    @Test
    fun testInsertAndGetMemory() = runBlocking {
        val memory = Memory(0, "John 3:16", "Amazing verse!", 1314, "Bruges")
        val id = memoryDao.insert(memory)
        val allMemories = memoryDao.getAllMemories()

        val retrievedMemory = allMemories.find { it.id == id }
        assertNotNull(retrievedMemory)
        assertEquals(memory.copy(id = id), retrievedMemory)
    }

    @Test
    fun testUpdateMemory() = runBlocking {
        val memory = Memory(1, "John 3:16", "Amazing verse!", 4932, "Bruges")
        val id = memoryDao.insert(memory)
        val updatedMemory = memory.copy(id = id, content = "Updated content")
        memoryDao.update(updatedMemory)
        val allMemories = memoryDao.getAllMemories()
        val retrievedMemory = allMemories.find { it.id == id }

        assertEquals(updatedMemory, retrievedMemory)
    }

    @Test
    fun testDeleteMemory() = runBlocking {
        val memory = Memory(2, "John 3:16", "Amazing verse!", 4314, "Bruges")
        val id = memoryDao.insert(memory)
        memoryDao.delete(id)
        val allMemories = memoryDao.getAllMemories()
        val retrievedMemory = allMemories.find { it.id == id }

        assertEquals(null, retrievedMemory)
    }
}