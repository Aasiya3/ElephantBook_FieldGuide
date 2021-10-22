package com.example.elephantbook_fieldguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
	val simpleImageView = findViewById<View>(R.id.simpleImageView) as ImageView

        //simpleImageView.setContentDescription("drawable/compressed_p1090053.jpg");
	//^^changes attribute contentDescription

        simpleImageView.setImageResource(R.drawable.compressed_p1090053)
	//^^changes attribute srcCompat
    }
}