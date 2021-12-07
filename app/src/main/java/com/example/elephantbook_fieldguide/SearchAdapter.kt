package com.example.elephantbook_fieldguide

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private var elephants: List<Elephant>, private val onClick: (Int?) -> Unit) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    class ViewHolder(view: View, private val onClick: (Int?) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
        var id: Int? = null

        init {
            view.setOnClickListener { onClick(id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_search_item, parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = elephants[position].toString()
        holder.id = elephants[position].id
    }

    override fun getItemCount(): Int {
        return elephants.size
    }

    // We really have no idea what has changed about the dataset, so just let Android handle it
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(new_elephants: List<Elephant>) {
        elephants = new_elephants
        notifyDataSetChanged()
    }
}