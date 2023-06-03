package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
}