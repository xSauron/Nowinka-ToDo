package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.database.EventModel

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventHolder>(){
    private var eventlist: ArrayList<EventModel> = ArrayList()
    private var OnClickDelete: ((EventModel) -> Unit)? = null

    fun addEvent(events: ArrayList<EventModel>) {
        this.eventlist = events
        notifyDataSetChanged()}

    fun setOnClickDelete(callback: (EventModel)->Unit){
        this.OnClickDelete = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EventHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.eventlayout, parent, false)
    )

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = eventlist[position]
        holder.bindView(event)
        holder.btnDelete.setOnClickListener{OnClickDelete?.invoke(event)}
    }

    override fun getItemCount(): Int {
        return eventlist.size
    }

    class EventHolder(var view: View): RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var title = view.findViewById<TextView>(R.id.tvTitle)
        private var desc = view.findViewById<TextView>(R.id.tvDesc)
        private var date = view.findViewById<TextView>(R.id.tvDate)
        private var prio = view.findViewById<TextView>(R.id.tvPrio)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)
        var btnEdit = view.findViewById<Button>(R.id.btnEdit)

        fun bindView(std:EventModel){
            id.text = std.event_id.toString()
            title.text = std.event_title
            desc.text = std.event_desc
            date.text = std.event_date.toString()
            prio.text = std.event_prio.toString()
        }

    }

}