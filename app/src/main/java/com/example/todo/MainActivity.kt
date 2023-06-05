package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.database.SQLHelper

class MainActivity : AppCompatActivity() {

    private lateinit var SQLHelper: SQLHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: EventAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addEventButton: Button = findViewById(R.id.addEventButton)
        recyclerView = findViewById(R.id.EventDisplay)

        initEvents()

        SQLHelper = SQLHelper(this)

        getEvents()

        addEventButton.setOnClickListener{
            val intent = Intent(this,AddEvent::class.java)
            startActivity(intent)
        }

        adapter?.setOnClickDelete { deleteEvent(it.event_id) }
    }

    private fun getEvents(){
        val eventlist = SQLHelper.displayEvents()
        Log.e("pppp","${eventlist.size}")

        adapter?.addEvent(eventlist)
    }

    private fun initEvents(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EventAdapter()
        recyclerView.adapter = adapter
    }

    private fun deleteEvent(event_id: Int){
        if(event_id == null) return

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Czy napewno chcesz usunąć to wydarzenie?")
        builder.setCancelable(true)
        builder.setNegativeButton("Nie") {dialog, _ -> dialog.dismiss()}
        builder.setPositiveButton("Tak") {dialog, _ ->
            SQLHelper.removeEvent(event_id)
            getEvents();
            dialog.dismiss()}

        val alert = builder.create()
        alert.show()
    }
}