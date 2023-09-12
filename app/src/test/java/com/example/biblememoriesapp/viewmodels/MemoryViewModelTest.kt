package com.example.biblememoriesapp.viewmodels

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.biblememoriesapp.data.Memory
import com.example.biblememoriesapp.data.repositories.MemoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.koinApplication
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Config.OLDEST_SDK])
class MemoryViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUpDispatchers() {
        Dispatchers.setMain(testDispatcher)
    }

    private var memoryRepository: MemoryRepository? = null
    private var memoryViewModel: MemoryViewModel? = null

    @Before
    fun setUp() {
        memoryRepository = mockk(relaxed = true)
        val application = ApplicationProvider.getApplicationContext<Application>()
        memoryViewModel = MemoryViewModel(memoryRepository!!, application = application)
    }



    @Test
    fun testUpdateMemory() = runTest{
        val memory = Memory(1, "Updated Verse", "Updated Content", 12345679, "Updated Location")
        memoryViewModel!!.updateMemory(memory)
        coVerify { memoryRepository!!.updateMemory(memory) }
    }

    @Test
    fun testDeleteMemory() = runTest{
        val memory = Memory(2, "Deleted Verse", "Deleted Content", 12345680, "Deleted Location")
        memoryViewModel!!.deleteMemory(memory.id)
        coVerify { memoryRepository!!.deleteMemory(memory.id) }
    }

    @Test
    fun testGetAllMemories() = runTest {
        val memory1 = Memory(0, "John 3:16", "Content", 12345678, "Location")
        val memory2 = Memory(1, "Updated Verse", "Updated Content", 12345679, "Updated Location")
        val memory3 = Memory(2, "Deleted Verse", "Deleted Content", 12345680, "Deleted Location")

        coEvery { memoryRepository!!.getAllMemories() } returns listOf(memory1, memory2, memory3)

        memoryViewModel!!.loadMemories()

        coVerify { memoryRepository!!.getAllMemories() }
    }



}