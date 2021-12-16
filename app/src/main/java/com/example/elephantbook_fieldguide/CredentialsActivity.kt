package com.example.elephantbook_fieldguide

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CredentialsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credentials)

        findViewById<EditText>(R.id.apiUrl).setText(Secrets.apiUrl)
        findViewById<EditText>(R.id.imageUrl).setText(Secrets.imageUrl)
        findViewById<EditText>(R.id.apiUsername).setText(Secrets.apiUsername)
        findViewById<EditText>(R.id.apiPassword).setText(Secrets.apiPassword)
        findViewById<Button>(R.id.submitButton).setOnClickListener {
            Secrets.updateSecrets(
                findViewById<EditText>(R.id.apiUrl).text.toString(),
                findViewById<EditText>(R.id.imageUrl).text.toString(),
                findViewById<EditText>(R.id.apiUsername).text.toString(),
                findViewById<EditText>(R.id.apiPassword).text.toString(),
                applicationContext
            )
            finish()
        }
    }
}