package com.r.events.view.ui.favourites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.r.events.R

class FavouritesAdapter(private val context: Context, private val events: List<Event>) : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.favourites_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        private lateinit var event: Event

        private val date = itemView?.findViewById<TextView?>(R.id.favourite_date)
        private val title = itemView?.findViewById<TextView?>(R.id.favourite_title)
        private val img = itemView?.findViewById<ImageView?>(R.id.event_image)

        fun bind(event: Event) {
            this.event = event
            date?.text = this.event.date
            title?.text = this.event.title
        }
    }
}