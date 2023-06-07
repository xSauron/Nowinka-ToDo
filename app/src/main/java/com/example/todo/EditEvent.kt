package com.example.todo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.database.EventModel
import com.example.todo.database.SQLHelper
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditEvent : AppCompatActivity() {
    private lateinit var sqlHelper: SQLHelper
    private lateinit var editButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var descEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var prioRatingBar: RatingBar
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
        prioRatingBar = findViewById(R.id.eventAddPrio)
        prioRatingBar.stepSize = 1.0f

        eventId = intent.getIntExtra("id", -1)

        val event = sqlHelper.getEvent(eventId)
        populateFields(event)

        editButton.setOnClickListener {
            val title = titleEditText.text.toString().trim()
            val desc = descEditText.text.toString().trim()
            val dateStr = dateEditText.text.toString().trim()
            val prio = prioRatingBar.rating.toInt()

            if (title.isEmpty()) {
                Toast.makeText(this, "Wprowadź tytuł", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dateStr.isEmpty()) {
                Toast.makeText(this, "Wprowadź datę", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidDate(dateStr)) {
                Toast.makeText(this, "Zły format daty. Format to DD-MM-RRRR", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentDate = Calendar.getInstance()
            currentDate.set(Calendar.HOUR_OF_DAY, 0)
            currentDate.set(Calendar.MINUTE, 0)
            currentDate.set(Calendar.SECOND, 0)
            currentDate.set(Calendar.MILLISECOND, 0)

            val selectedDate = getCalendarFromDateString(dateStr)

            if (selectedDate == null || selectedDate.before(currentDate)) {
                Toast.makeText(this, "Data nie może być starsza od aktualnej", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedEvent = EventModel(event_id = eventId, event_title = title, event_desc = desc, event_date = formatDate(dateStr), event_prio = prio)
            val rowsAffected = sqlHelper.updateEvent(updatedEvent)



            if (rowsAffected > 0)
            {
                Toast.makeText(this, "Zedytowano wydarzenie", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

    private fun populateFields(event: EventModel?) {
        event?.let {
            titleEditText.setText(event.event_title)
            descEditText.setText(event.event_desc)
            dateEditText.setText(event.event_date)
            prioRatingBar.rating = event.event_prio.toFloat()
        }
    }

    private fun formatDate(dateStr: String): String {
        val date = dateFormat.parse(dateStr)
        return dateFormat.format(date)
    }

    private fun isValidDate(dateStr: String): Boolean {
        return try {
            dateFormat.parse(dateStr)
            true
        } catch (e: ParseException) {
            false
        }
    }

    private fun getCalendarFromDateString(dateStr: String): Calendar? {
        return try {
            val date = dateFormat.parse(dateStr)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar
        } catch (e: ParseException) {
            null
        }
    }
}