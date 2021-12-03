package com.example.elephantbook_fieldguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private lateinit var adapter: RecyclerAdapter
private lateinit var linearLayoutManager: LinearLayoutManager


class SeekSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek_search)

        linearLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = linearLayoutManager

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra("searchBy")!!

        val databaseWrapper = DatabaseWrapper.create(applicationContext)
        val listElephants = databaseWrapper.getElephantsBySeek(message)
    }





}