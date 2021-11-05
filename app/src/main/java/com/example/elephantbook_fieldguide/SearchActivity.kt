package com.example.elephantbook_fieldguide

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var searchMethod: (String) -> List<Elephant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val databaseWrapper = DatabaseWrapper.create(applicationContext)

        searchMethod = when (intent.getStringExtra("searchBy")!!) {
            "Name" -> databaseWrapper::getElephantsByNamePrefix
            "SEEK" -> databaseWrapper::getElephantsBySeek
            else -> throw IllegalArgumentException("Illegal searchBy method!")
        }

        val resultsView = findViewById<TextView>(R.id.results)
        findViewById<EditText>(R.id.query).run {
            hint = intent.getStringExtra("searchBy")
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    resultsView.text = searchMethod(p0.toString()).joinToString("\n")
                }
            })
        }

        findViewById<Button>(R.id.displayButton).setOnClickListener {
            val elephants = searchMethod(findViewById<EditText>(R.id.query).text.toString())
            displayIndividual(
                if (elephants.isNotEmpty()) {
                    elephants[0].id
                } else {
                    -1
                }
            )
        }

        resultsView.text = searchMethod("").joinToString("\n")
    }

    private fun displayIndividual(id: Int) {
        startActivity(Intent(this, IndividualActivity::class.java).apply {
            putExtra("elephantId", id)
        })
    }
}