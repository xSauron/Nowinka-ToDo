package com.example.todo.database

data class EventModel (
    var event_id: Int,
    var event_title: String,
    var event_desc: String,
    var event_date: Int,
    var event_prio: Int
)