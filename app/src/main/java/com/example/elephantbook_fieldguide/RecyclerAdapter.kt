package com.example.elephantbook_fieldguide

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val elephants: ArrayList<Elephant>) :
    RecyclerView.Adapter<RecyclerAdapter.ElephantHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ElephantHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ElephantHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
        //get the number of elephants in the database
        //example: override fun getItemCount() = photos.size
        //https://www.raywenderlich.com/1560485-android-recyclerview-tutorial-with-kotlin
        //do later when I hear back from brandon about how to get the size.
    }

    class ElephantHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener{
        private var view: View = v
        private var elepahants: Elephant? = null
        init {
            v.setOnClickListener { this }
        }
        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
            //This should call an intent for the individual view
        }
        companion object {
            private val ELEPHANTS_KEY = "ELEPHANT"
        }
    }

}
