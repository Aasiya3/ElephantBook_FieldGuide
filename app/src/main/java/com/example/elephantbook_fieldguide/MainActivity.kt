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
        val apiGetter = ApiGetter(applicationContext)
        val path = "elephant5.jpg"
        apiGetter.downloadImage(
            "https://media-cldnry.s-nbcnews.com/image/upload/newscms/2021_42/3514300/211022-mozambique-elephants-mb-0945.jpg",
            path
        ) {
            val myDraw = Drawable.createFromStream(applicationContext.openFileInput(path), path)
            simpleImageView.setImageDrawable(myDraw)
            //^^changes attribute srcCompat
        }
        // TESTING CODE

    }
}