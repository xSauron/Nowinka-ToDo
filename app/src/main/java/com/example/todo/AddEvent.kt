package com.example.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.database.EventModel
import com.example.todo.database.SQLHelper

class AddEvent : AppCompatActivity() {
    private lateinit var SQLHelper: SQLHelper
    private lateinit var addButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var prioEditText: EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_event)

        SQLHelper = SQLHelper(this)

        addButton = findViewById(R.id.addEventBtn)
        titleEditText  = findViewById(R.id.eventAddTitle)
        descEditText  = findViewById(R.id.eventAddDesc)
        dateEditText  = findViewById(R.id.eventAddDate)
        prioEditText  = findViewById(R.id.eventAddPrio)

        addButton.setOnClickListener {

            val title = titleEditText.text.toString()
            val desc = descEditText.text.toString()
            val date = dateEditText.text.toString().toIntOrNull() ?: 0
            val prio = prioEditText.text.toString().toIntOrNull() ?: 0

            val eventModel = EventModel(event_title = title, event_desc = desc, event_date = date, event_prio = prio)
            SQLHelper.addEvent(eventModel)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }

    }
}