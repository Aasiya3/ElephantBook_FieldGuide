package com.example.elephantbook_fieldguide

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Secrets.initializeSecrets(applicationContext)
        val databaseWrapper = DatabaseWrapper.create(applicationContext)

        findViewById<Button>(R.id.nameSearch).setOnClickListener {
            startActivity(
                Intent(this, SearchActivity::class.java).apply {
                    putExtra(getString(R.string.searchBy), getString(R.string.searchByName))
                }
            )
        }

        findViewById<Button>(R.id.seekSearch).setOnClickListener {
            startActivity(
                Intent(this, SearchActivity::class.java).apply {
                    putExtra(getString(R.string.searchBy), getString(R.string.searchBySeek))
                }
            )
        }

        findViewById<Button>(R.id.updateDatabase).setOnClickListener {
            Toast.makeText(applicationContext, "Updating database...", Toast.LENGTH_SHORT).show()
            databaseWrapper.updateDatabase {
                Toast.makeText(
                    applicationContext,
                    when (it) {
                        DatabaseWrapper.Companion.UpdateStatus.SUCCESS -> "Database updated!"
                        DatabaseWrapper.Companion.UpdateStatus.BAD_API -> "Update failed (API)"
                        DatabaseWrapper.Companion.UpdateStatus.BAD_IMAGES -> "Update failed (Images)"
                    },
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        findViewById<Button>(R.id.changeCredentials).setOnClickListener {
            startActivity(
                Intent(this, CredentialsActivity::class.java)
            )
        }

    }
}