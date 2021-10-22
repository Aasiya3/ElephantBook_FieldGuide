package com.example.elephantbook_fieldguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbW = DatabaseWrapper(applicationContext)
        println(dbW.getAllLocations())
    }
}