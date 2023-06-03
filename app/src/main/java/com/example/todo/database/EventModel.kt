package com.example.todo.database

object EventIdGenerator {
    private var lastEventId = 0
    fun generateEventId(): Int {
        lastEventId++
        return lastEventId
    }
}

data class EventModel(
    var event_id: Int = EventIdGenerator.generateEventId(),
    var event_title: String,
    var event_desc: String,
    var event_date: Int,
    var event_prio: Int
)