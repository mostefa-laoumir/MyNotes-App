package com.example.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DBManager {
    val dbName = "MyNotes"
    val dbTable = "Notes"
    val colID = "ID"
    val colTitle = "Title"
    val colDes = "Description"
    val dbVersion = 1
    val SQLCreateTable = "CREATE TABLE IF NOT EXISTS "+dbTable+" ("+colID+" INTEGER PRIMARY KEY , "+
            colTitle+" TEXT, "+colDes+" TEXT);"

    var sqlDB:SQLiteDatabase?= null


    constructor(context: Context) {
        val db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes: SQLiteOpenHelper {
         var context:Context?= null
        constructor(context:Context):super(context, dbName, null, dbVersion){
            this.context = context
        }
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(SQLCreateTable)
            Toast.makeText(this.context,"DataBase is Created",Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
           db!!.execSQL("Drop table IF EXISTS "+dbTable)

        }
    }


    fun insert(value: ContentValues):Long{
        val ID = sqlDB!!.insert(dbTable,"",value)
        return ID
    }
    fun query(projection:Array<String>,selection:String, selectionArg:Array<String>, sortOrder:String): Cursor {
         val q = SQLiteQueryBuilder()
        q.tables = dbTable
        val cursor = q.query(sqlDB, projection, selection,  selectionArg,null, null, sortOrder)
            return cursor
    }
    fun delete(selection:String, selectionArg:Array<String>):Int{
            val count = sqlDB!!.delete(dbTable,selection,selectionArg)
                return count
    }
    fun update(values: ContentValues, selection: String, selectionArg: Array<String>):Int{
        val count = sqlDB!!.update(dbTable, values, selection, selectionArg)
        return count
    }
}