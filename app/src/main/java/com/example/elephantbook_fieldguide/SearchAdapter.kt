package com.example.elephantbook_fieldguide

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private var elephants: List<Elephant>, private val databaseWrapper: DatabaseWrapper, private val onClick: (Int?) -> Unit) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    class ViewHolder(private val view: View, private val databaseWrapper: DatabaseWrapper, private val onClick: (Int?) -> Unit) :
        RecyclerView.ViewHolder(view) {
        var elephant: Elephant? = null

        init {
            view.setOnClickListener { onClick(elephant?.id) }
        }

        fun updateView(newElephant: Elephant) {
            elephant = newElephant
            view.findViewById<TextView>(R.id.elephantName).text = elephant?.name
            view.findViewById<TextView>(R.id.seekCode).text = elephant?.seek
            view.findViewById<TextView>(R.id.lastSeen).text =
                elephant?.id?.let { databaseWrapper.getLatestLocation(it)?.dateTime.toString() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_search_item, parent, false),
            databaseWrapper,
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.updateView(elephants[position])
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