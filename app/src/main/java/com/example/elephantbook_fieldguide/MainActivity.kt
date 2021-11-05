package com.example.elephantbook_fieldguide

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val databaseWrapper = DatabaseWrapper.create(applicationContext)

        findViewById<Button>(R.id.seekButton).setOnClickListener { searchBy(it) }
        findViewById<Button>(R.id.nameButton).setOnClickListener { searchBy(it) }
        findViewById<Button>(R.id.updateDatabaseButton).setOnClickListener {
            databaseWrapper.updateDatabase {
                Toast.makeText(
                    applicationContext,
                    "Database Updated",
                    Toast.LENGTH_LONG
                ).show()
                println("Database Updated")
            }
        }
    }

    private fun searchBy(view: View) {
        startActivity(Intent(this, SearchActivity::class.java).apply {
            putExtra(
                "searchBy",
                when (view.id) {
                    R.id.seekButton -> "SEEK"
                    R.id.nameButton -> "Name"
                    else -> throw IllegalStateException("Something called me that's not allowed to")
                }
            )
        })
    }
}