package com.example.todo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.database.EventModel

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventHolder>(){
    private var eventlist: ArrayList<EventModel> = ArrayList()
    private var onClickDelete: ((EventModel) -> Unit)? = null
    private var onClickEdit: ((EventModel) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addEvent(events: ArrayList<EventModel>) {
        this.eventlist = events
        notifyDataSetChanged()}

    fun setOnClickDelete(callback: (EventModel)->Unit){
        this.onClickDelete = callback
    }

    fun setOnClickEdit(callback: (EventModel)->Unit){
        this.onClickEdit = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EventHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.eventlayout, parent, false)
    )

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = eventlist[position]
        holder.bindView(event)
        holder.btnDelete.setOnClickListener{onClickDelete?.invoke(event)}
        holder.btnEdit.setOnClickListener{onClickEdit?.invoke(event)}
    }

    override fun getItemCount(): Int {
        return eventlist.size
    }

    class EventHolder(view: View): RecyclerView.ViewHolder(view){
        private var title = view.findViewById<TextView>(R.id.tvTitle)
        private var desc = view.findViewById<TextView>(R.id.tvDesc)
        private var date = view.findViewById<TextView>(R.id.tvDate)
        private var time = view.findViewById<TextView>(R.id.tvTime)
        private var prio = view.findViewById<TextView>(R.id.tvPrio)
        var btnDelete: Button = view.findViewById(R.id.btnDelete)
        var btnEdit: Button = view.findViewById(R.id.btnEdit)

        fun bindView(std:EventModel){
            title.text = std.event_title
            desc.text = std.event_desc
            date.text = std.event_date.toString()
            time.text = std.event_time.toString()
            prio.text = std.event_prio.toString()
        }

    }

}