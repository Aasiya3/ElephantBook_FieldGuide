package com.example.elephantbook_fieldguide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DatabaseWrapper.create(applicationContext).updateDatabase {
            //findViewById<Button>(R.id.button).setOnClickListener { showIndividual(it) }
            findViewById<Button>(R.id.button2).setOnClickListener {
                startActivity(
                    Intent(this, SearchActivity::class.java).apply {
                        putExtra("searchBy", "Name")
                    }
                )
            }
        }
    }


}