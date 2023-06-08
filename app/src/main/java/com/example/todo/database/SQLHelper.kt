package com.example.todo.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SQLHelper(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "todo.db"
        private const val TABLE = "events"
        private const val ID = "event_id"
        private const val TITLE = "event_title"
        private const val DESC = "event_desc"
        private const val DATE = "event_date"
        private const val TIME = "event_time"
        private const val PRIO = "event_prio"
    }

    override fun onCreate(database: SQLiteDatabase?) {
        val crateTable =
            ("CREATE TABLE IF NOT EXISTS $TABLE($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE TEXT, $DESC TEXT, $DATE INTEGER, $TIME TEXT, $PRIO INTEGER )")
        database?.execSQL(crateTable)
    }

    override fun onUpgrade(database: SQLiteDatabase?, old: Int, new: Int) {
        database!!.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(database)
    }

    fun addEvent(std: EventModel): Long {
        val database = this.writableDatabase

        val data = ContentValues()
        data.put(TITLE, std.event_title)
        data.put(DESC, std.event_desc)
        data.put(DATE, convertToTimestamp(std.event_date))
        data.put(TIME, std.event_time)
        data.put(PRIO, std.event_prio)

        val insert = database.insert(TABLE, null, data)
        database.close()
        return insert
    }

    @SuppressLint("Range", "Recycle")
    fun displayEvents(): ArrayList<EventModel> {
        val events: ArrayList<EventModel> = ArrayList()
        val query = "SELECT * FROM $TABLE"
        val database = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = database.rawQuery(query, null)
        } catch (error: Exception) {
            error.printStackTrace()
            database.execSQL(query)
            return ArrayList()
        }

        var id: Int
        var title: String
        var desc: String
        var date: Long
        var time: String
        var prio: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("event_id"))
                title = cursor.getString(cursor.getColumnIndex("event_title"))
                desc = cursor.getString(cursor.getColumnIndex("event_desc"))
                date = cursor.getLong(cursor.getColumnIndex("event_date"))
                time = cursor.getString(cursor.getColumnIndex("event_time"))
                prio = cursor.getInt(cursor.getColumnIndex("event_prio"))

                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(date))

                val event = EventModel(
                    event_id = id,
                    event_title = title,
                    event_desc = desc,
                    event_date = formattedDate,
                    event_time = time,
                    event_prio = prio
                )
                events.add(event)
            } while (cursor.moveToNext())
        }
        database.close()
        return events
    }

    fun removeEvent(id: Int): Int{
        val database = this.readableDatabase
        val content = ContentValues()
        content.put(ID,id)

        val event = database.delete(TABLE, "event_id=$id",null)
        database.close()
        return event
    }

    fun updateEvent(Event: EventModel): Int {
        val database = this.writableDatabase

        val data = ContentValues()
        data.put(TITLE, Event.event_title)
        data.put(DESC, Event.event_desc)
        data.put(DATE, convertToTimestamp(Event.event_date))
        data.put(TIME, Event.event_time)
        data.put(PRIO, Event.event_prio)

        val event = database.update(TABLE, data, "event_id=" + Event.event_id, null)
        database.close()
        return event
    }

    @SuppressLint("Range")
    fun getEvent(eventId: Int): EventModel? {
        val query = "SELECT * FROM $TABLE WHERE $ID = $eventId"
        val database = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = database.rawQuery(query, null)
        } catch (error: Exception) {
            error.printStackTrace()
            database.execSQL(query)
            return null
        }

        var event: EventModel? = null

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex("event_id"))
            val title = cursor.getString(cursor.getColumnIndex("event_title"))
            val desc = cursor.getString(cursor.getColumnIndex("event_desc"))
            val date = cursor.getLong(cursor.getColumnIndex("event_date"))
            val time = cursor.getString(cursor.getColumnIndex("event_time"))
            val prio = cursor.getInt(cursor.getColumnIndex("event_prio"))

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(date))

            event = EventModel(
                event_id = id,
                event_title = title,
                event_desc = desc,
                event_date = formattedDate,
                event_time = time,
                event_prio = prio
            )
        }

        cursor.close()
        database.close()

        return event
    }

    private fun convertToTimestamp(date: String): Long {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val convertedDate = dateFormat.parse(date)
        return convertedDate?.time ?: 0
    }
}