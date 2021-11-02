package com.example.elephantbook_fieldguide

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener { showIndividual() }

    }

    private fun showIndividual(view: View) {
        startActivity(Intent(this, IndividualActivity::class.java).apply {
            putExtra(
                "elephantId",
                5
            )
        })
    }
}