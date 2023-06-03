package com.example.todo.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class SQLHelper(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "todo.db"
        private const val TABLE = "events"
        private const val ID = "event_id"
        private const val TITLE = "event_title"
        private const val DESC = "event_desc"
        private const val DATE = "event_date"
        private const val PRIO = "event_prio"

    }

    override fun onCreate(database: SQLiteDatabase?) {
        val crateTable =
            ("CREATE TABLE " + TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TITLE + " TEXT, " + DESC + " TEXT, " + DATE + " INTEGER, " + PRIO + " INTEGER " + ")")
        database?.execSQL(crateTable)
    }

    override fun onUpgrade(database: SQLiteDatabase?, old: Int, new: Int) {
        database!!.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(database)
    }

    fun addEvent(std: EventModel): Long {
        val database = this.writableDatabase

        val data = ContentValues()
        data.put(ID, std.event_id)
        data.put(TITLE, std.event_title)
        data.put(DESC, std.event_desc)
        data.put(DATE, std.event_date)

        val insert = database.insert(TABLE, null, data)
        database.close()
        return insert
    }

    @SuppressLint("Range")
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
        var date: Int
        var prio: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("event_id"))
                title = cursor.getString(cursor.getColumnIndex("event_title"))
                desc = cursor.getString(cursor.getColumnIndex("event_desc"))
                date = cursor.getInt(cursor.getColumnIndex("event_date"))
                prio = cursor.getInt(cursor.getColumnIndex("event_prio"))

                val event = EventModel(
                    event_id = id,
                    event_title = title,
                    event_desc = desc,
                    event_date = date,
                    event_prio = prio
                )
                events.add(event)
            } while (cursor.moveToNext())
        }
        database.close()
        return events
    }

    fun removeEvent(ID: Int) {
        val database = this.readableDatabase
        val clause = "$ID = ?"
        val argument = arrayOf(ID.toString())

        database.delete(TABLE, clause, argument)
        database.close()
    }

    fun updateEvent(Event: EventModel) {
        val database = this.writableDatabase

        val values = ContentValues()
        values.put(TITLE, Event.event_title)
        values.put(DESC, Event.event_desc)
        values.put(DATE, Event.event_date)
        values.put(PRIO, Event.event_prio)

        val clause = "$ID = ?"
        val argument = arrayOf(Event.event_id.toString())

        val updatedCount = database.update(TABLE, values, clause, argument)
        database.close()
    }
}