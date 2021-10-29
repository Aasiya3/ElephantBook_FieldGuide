package com.example.elephantbook_fieldguide

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.room.Room
import kotlinx.coroutines.*
import java.io.File
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val simpleImageView = findViewById<View>(R.id.simpleImageView) as ImageView

        // TESTING CODE
        val dbW = DatabaseWrapper(applicationContext)
        dbW.updateDatabase {
            simpleImageView.setImageDrawable(dbW.getElephantPfp(5))
            println(dbW.getElephantsByNamePrefix("F"))
            println(dbW.getLatestLocation(5))
        }
        // TESTING CODE

    }
}