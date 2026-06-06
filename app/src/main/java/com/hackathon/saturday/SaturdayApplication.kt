package com.hackathon.saturday

import android.app.Application
import com.hackathon.saturday.data.local.db.SaturdayDatabase
import com.hackathon.saturday.data.repository.ActionRepository

class SaturdayApplication : Application() {

    val database by lazy { SaturdayDatabase.getInstance(this) }
    val repository by lazy {
        ActionRepository(
            database.taskDao(),
            database.deadlineDao(),
            database.eventDao(),
            database.flashcardDao()
        )
    }

    companion object {
        lateinit var instance: SaturdayApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
