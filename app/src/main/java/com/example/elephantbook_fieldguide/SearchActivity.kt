package com.example.elephantbook_fieldguide

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// https://github.com/android/views-widgets-samples/blob/main/RecyclerViewKotlin/app/src/main/java/com/example/recyclersample/flowerList/FlowersListActivity.kt
// https://github.com/android/views-widgets-samples/blob/main/RecyclerViewKotlin/app/src/main/java/com/example/recyclersample/flowerList/FlowersAdapter.kt
class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val databaseWrapper = DatabaseWrapper.create(applicationContext)
        val searchAdapter = SearchAdapter(databaseWrapper.getAllElephants(), ::showIndividual)
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }

        val searchMethod = when (intent.getStringExtra("searchBy")!!) {
            "Name" -> databaseWrapper::getElephantsByNamePrefix
            "SEEK" -> databaseWrapper::getElephantsBySeek
            else -> throw IllegalArgumentException("Illegal searchBy method!")
        }

        findViewById<EditText>(R.id.query).run {
            hint = intent.getStringExtra("searchBy")
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    searchAdapter.updateData(searchMethod(p0.toString()))
                }
            })
        }
    }

    private fun showIndividual(id: Int?) {
        if (id == null) return

        startActivity(Intent(this, IndividualActivity::class.java).apply {
            putExtra("elephantId", id)
        })
    }
}