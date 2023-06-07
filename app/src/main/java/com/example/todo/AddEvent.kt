package com.example.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.database.EventModel
import com.example.todo.database.SQLHelper
import java.text.SimpleDateFormat
import java.util.Locale

class AddEvent : AppCompatActivity() {
    private lateinit var sqlHelper: SQLHelper
    private lateinit var addButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var prioRatingBar: RatingBar

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_event)

        sqlHelper = SQLHelper(this)

        addButton = findViewById(R.id.editEventBtn)
        titleEditText  = findViewById(R.id.eventAddTitle)
        descEditText  = findViewById(R.id.eventAddDesc)
        dateEditText  = findViewById(R.id.eventAddDate)
        prioRatingBar  = findViewById(R.id.eventAddPrio)
        prioRatingBar.stepSize = 1.0f

        addButton.setOnClickListener {

            val title = titleEditText.text.toString()
            val desc = descEditText.text.toString()
            val dateStr = dateEditText.text.toString()
            val date = formatDate(dateStr)
            val prio = prioRatingBar.rating.toInt()

            val eventModel = EventModel(event_title = title, event_desc = desc, event_date = date, event_prio = prio)
            sqlHelper.addEvent(eventModel)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }

    }

    private fun formatDate(dateStr: String): String {
        val date = dateFormat.parse(dateStr)
        return dateFormat.format(date)
    }
}