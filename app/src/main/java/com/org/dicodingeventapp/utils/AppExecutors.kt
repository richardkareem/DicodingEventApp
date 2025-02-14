package com.org.dicodingeventapp.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class AppExecutors {
    val diskIO: Executor = Executors.newSingleThreadExecutor() //membaca atau menulis ke db
    // menjalankan tugas dengan 3 thread (menjalankan 3 tugas sekaligus bersamaan)
    val networkIO: Executor = Executors.newFixedThreadPool(3)
    // menjalankan tugas di main thread
    val mainThread: Executor = MainThreadExecutor()

    private class MainThreadExecutor : Executor {
        // memastikan tugas di eksekusi di main thread
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

}