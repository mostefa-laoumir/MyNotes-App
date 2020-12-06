package com.example.noteapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note.*
import java.lang.Exception

class NoteActivity : AppCompatActivity() {
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        var bundle = intent.extras
        try {
            id = bundle!!.getInt("ID", 0)
        }catch (e:Exception){

        }

        if(id!=0){
            val title = bundle!!.getString("Title")
            titleID.setText(title.toString())
            val des = bundle!!.getString("Des")
            desID.setText(des.toString())
        }
    }
    fun addButton(view:View){
        val dbManager = DBManager(this)
        val values = ContentValues()
        values.put("Title", titleID.text.toString())
        values.put("Description", desID.text.toString())

        if (id==0){
        val ID = dbManager.insert(values)
        if(ID>0){
            Toast.makeText(this,"Note Added!", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Cannot Add Note!", Toast.LENGTH_LONG).show()

        } }else{
            val selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values,"ID = ?", selectionArgs )
            if(ID>0){
                Toast.makeText(this,"Note Updated!", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Cannot Update Note!", Toast.LENGTH_LONG).show()

            }
        }

        finish()
    }
}