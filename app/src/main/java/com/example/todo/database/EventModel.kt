package com.example.todo.database

data class EventModel(
    var event_id: Int? = null,
    var event_title: String,
    var event_desc: String,
    var event_date: String,
    var event_prio: Int
)