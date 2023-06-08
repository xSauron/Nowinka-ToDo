package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.database.SQLHelper

class MainActivity : AppCompatActivity() {

    private lateinit var sqlHelper: SQLHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: EventAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addEventButton: Button = findViewById(R.id.addEventButton)
        recyclerView = findViewById(R.id.EventDisplay)

        sqlHelper = SQLHelper(this)
        sqlHelper.onCreate(sqlHelper.writableDatabase)

        /*
        * Po jakiejkolwiek zmianie stróktury bazy danych znależy odkomentować i uruchomić to polecenie, inaczej aplikacja nie będzie działać.
        * Spowoduje one usunięcie aktualnej bazy danych i pozwoli utworzyć nową.
        * Po udanym ponownym uruchomieniu należy ją zakomentować
        *
         */
        //sqlHelper.onUpgrade(sqlHelper.writableDatabase,0,0);

        initEvents()
        getEvents()

        addEventButton.setOnClickListener{
            val intent = Intent(this,AddEvent::class.java)
            startActivity(intent)
        }

        adapter?.setOnClickDelete { it.event_id?.let { it1 -> deleteEvent(it1) } }
        adapter?.setOnClickEdit {
            val intent = Intent(this,EditEvent::class.java)
            intent.putExtra("id",it.event_id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getEvents()
    }

    private fun getEvents(){
        val eventlist = sqlHelper.displayEvents()
        Log.e("pppp","${eventlist.size}")

        adapter?.addEvent(eventlist)
    }

    private fun initEvents(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = EventAdapter()
        recyclerView.adapter = adapter
    }

    private fun deleteEvent(event_id: Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Czy napewno chcesz usunąć to wydarzenie?")
        builder.setCancelable(true)
        builder.setNegativeButton("Nie") {dialog, _ -> dialog.dismiss()}
        builder.setPositiveButton("Tak") {dialog, _ ->
            sqlHelper.removeEvent(event_id)
            getEvents()
            Toast.makeText(this, "Wydarzenie usunięte", Toast.LENGTH_SHORT).show()
            dialog.dismiss()}

        val alert = builder.create()
        alert.show()
    }
}