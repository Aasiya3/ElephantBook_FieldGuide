package com.example.elephantbook_fieldguide

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
//Changed ArrayList to List, don't know if that will cause a problem
class RecyclerAdapter(private val elephants: List<Elephant>) :
    RecyclerView.Adapter<RecyclerAdapter.ElephantHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ElephantHolder {
        val inflatedView = parent.inflate(R.layout.seek_recyclerview_item_row, false)
        return ElephantHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ElephantHolder, position: Int) {
        val itemElephant = elephants[position]
        holder.bindElephant(itemElephant)
    }

    override fun getItemCount() = elephants.size

    class ElephantHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener{
        private var view: View = v
        private var elephants: Elephant? = null
        init {
            v.setOnClickListener { this }
        }
        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
            //This should call an intent for the individual view
            val context = itemView.context
            val individualViewIntent = Intent(context, IndividualActivity::class.java)
            individualViewIntent.putExtra(ELEPHANTS_KEY, (elephants!!).id)
            context.startActivity(individualViewIntent)

        }
        companion object {
            private val ELEPHANTS_KEY = "elephantId"
        }

        fun bindElephant(elephants: Elephant) {
            this.elephants = elephants
            //Picasso.with(view.context).load(elephants.url).into(view.itemImage)
            view.elephantName.txt = elephants.name
            //view.lastSeen.txt = Last seen
            view.Seek.txt = elephants.seek
        }

    }

}
