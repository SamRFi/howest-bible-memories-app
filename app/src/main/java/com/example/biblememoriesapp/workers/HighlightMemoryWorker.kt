package com.example.biblememoriesapp.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.biblememoriesapp.R
import com.example.biblememoriesapp.data.BibleMemoriesDatabase
import com.example.biblememoriesapp.data.Memory
import com.example.biblememoriesapp.data.MemoryDao
import com.example.biblememoriesapp.data.repositories.MemoryRepository
import kotlinx.coroutines.coroutineScope
import java.util.Calendar


class HighlightMemoryWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val memoryDao: MemoryDao by lazy {
        val database: BibleMemoriesDatabase = BibleMemoriesDatabase.getInstance(applicationContext)
        database.memoryDao()
    }
    private val memoryRepository = MemoryRepository(memoryDao)

    override suspend fun doWork(): Result = coroutineScope {
        try {

            val calendar = Calendar.getInstance().apply {
                //add(Calendar.MONTH, -1)
                add(Calendar.DAY_OF_MONTH, 0)

                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfMonthAgo = calendar.timeInMillis

            calendar.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }
            val endOfMonthAgo = calendar.timeInMillis

            val memories: List<Memory> = memoryRepository.getMemoriesFromTimeAgo(startOfMonthAgo, endOfMonthAgo)

            if (memories.isEmpty()) {
                memoryRepository.unHighlightAllMemories()
                Log.e("HighlightMemoryWorker", "No memories found.")
                return@coroutineScope Result.failure()
            }


            val memoryToHighlight = memories.random()
            memoryToHighlight.isHighlighted = true
            memoryRepository.unHighlightAllMemories()
            memoryRepository.updateMemory(memoryToHighlight)
            showNotification(applicationContext, memoryToHighlight)


            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}

private fun showNotification(context: Context, memory: Memory) {
    val channelId = "memory_notification_channel"
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_book_foreground)
        .setContentTitle("Memory from a month ago")
        .setContentText(memory.verse)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Memory Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(1, notificationBuilder.build())
}

