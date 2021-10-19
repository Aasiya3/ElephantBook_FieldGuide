package com.example.elephantbook_fieldguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
	val simpleImageView = findViewById<View>(R.id.simpleImageView) as ImageView
        
	simpleImageView.setContentDescription(R.drawable."compressed_IMG_5085")
	//^^changes attribute contentDescription

        simpleImageView.setImageResource(R.drawable."compressed_IMG_5085")
	//^^changes attribute srcCompat
    }
}