package com.example.noteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_note.view.*
import kotlinx.android.synthetic.main.ticket.view.*


class MainActivity : AppCompatActivity() {

    var listOfNotes= ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get db notes
        loadData("%")





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val sv = menu!!.findItem(R.id.searchNote).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                //search in database
                loadData("%"+query+"%")

                return false
                                    }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(applicationContext, newText, Toast.LENGTH_LONG).show()

                return false
                                       }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        loadData("%")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addNote->{
                //go to add page
                var intent = Intent(this,NoteActivity::class.java)
                startActivity(intent)
                            }

        }
        return super.onOptionsItemSelected(item)
    }



    //listview adapter
    inner class NoteAdapter:BaseAdapter{
        var context:Context? = null
        var listOfNotes= ArrayList<Note>()
        constructor(context: Context, listAOfNote:ArrayList<Note>):super(){
            this.listOfNotes = listAOfNote
            this.context = context
                    }

        override fun getCount(): Int {
        return listOfNotes.size
        }

        override fun getItem(position: Int): Any {
                return this.listOfNotes[position]
        }

        override fun getItemId(position: Int): Long {
                return position.toLong()
            }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.ticket,null)
            var myNote = listOfNotes[position]
            myView.titleTextView.text = myNote.noteName
            myView.des.text = myNote.noteDes
            myView.deleteIV.setOnClickListener {
            var dbManager = DBManager(this.context!!)
                val selectioArgs = arrayOf(myNote.noteID.toString())
                dbManager.delete("ID = ?",selectioArgs)
                loadData("%")
                Toast.makeText(context,myNote.noteName+" is Deleted!",Toast.LENGTH_SHORT).show()
            }
            myView.editIV.setOnClickListener {
                goToUpdate(myNote)
            }
            return myView
        }

    }
    fun loadData(title: String){
        var dbManager = DBManager(this)
        var selectionArgs = arrayOf(title)
        val projection = arrayOf("ID","Title","Description")
        var cursor = dbManager.query(projection, "Title like ?", selectionArgs,"")

        listOfNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Des = cursor.getString(cursor.getColumnIndex("Description"))
                listOfNotes.add(Note(ID,Title,Des))
            }while (cursor.moveToNext())
        }
        var myNotesAdapter = NoteAdapter(this,listOfNotes)
        notesListView.adapter = myNotesAdapter


    }
    fun goToUpdate(note: Note){
        var intent = Intent(this,NoteActivity::class.java)
        intent.putExtra("ID",note.noteID)
        intent.putExtra("Title",note.noteName)
        intent.putExtra("Des",note.noteDes)
        startActivity(intent)

    }

}