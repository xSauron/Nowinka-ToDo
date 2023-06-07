package com.example.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.database.EventModel
import com.example.todo.database.SQLHelper
import java.text.SimpleDateFormat
import java.util.Locale

class EditEvent : AppCompatActivity() {
    private lateinit var sqlHelper: SQLHelper
    private lateinit var editButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var prioEditText: EditText
    private var eventId: Int = -1

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_event)

        sqlHelper = SQLHelper(this)

        editButton = findViewById(R.id.editEventBtn)
        titleEditText = findViewById(R.id.eventAddTitle)
        descEditText = findViewById(R.id.eventAddDesc)
        dateEditText = findViewById(R.id.eventAddDate)
        prioEditText = findViewById(R.id.eventAddPrio)

        eventId = intent.getIntExtra("id", -1)

        val event = sqlHelper.getEvent(eventId)
        populateFields(event)

        editButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val desc = descEditText.text.toString()
            val dateStr = dateEditText.text.toString()
            val date = formatDate(dateStr)
            val prio = prioEditText.text.toString().toInt()

            val updatedEvent = EventModel(event_id = eventId, event_title = title, event_desc = desc, event_date = date, event_prio = prio)
            val rowsAffected = sqlHelper.updateEvent(updatedEvent)

            if (rowsAffected > 0) finish()
        }

    }

    private fun populateFields(event: EventModel?) {
        event?.let {
            titleEditText.setText(event.event_title)
            descEditText.setText(event.event_desc)
            dateEditText.setText(event.event_date.toString())
            prioEditText.setText(event.event_prio.toString())
        }
    }

    private fun formatDate(dateStr: String): String {
        val date = dateFormat.parse(dateStr)
        return dateFormat.format(date)
    }
}