package com.example.biblememoriesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.biblememoriesapp.ui.composables.Navigation
import com.example.biblememoriesapp.ui.theme.BibleMemoriesAppTheme
import androidx.work.WorkManager
import com.example.biblememoriesapp.workers.HighlightMemoryWorker
import androidx.work.PeriodicWorkRequestBuilder
import com.example.biblememoriesapp.viewmodels.MainViewModel
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BibleMemoriesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // pass ViewModel instead of Dao and Repository
                    Navigation(mainViewModel)
                }
            }
        }

        scheduleHighlightMemoryWorker()
    }

    private fun scheduleHighlightMemoryWorker() {
        val workRequest = PeriodicWorkRequestBuilder<HighlightMemoryWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BibleMemoriesAppTheme {
    }
}