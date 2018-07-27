package com.elegantappstore.mynotes.Controlers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {
    // Define variables to create database:
    val dbName ="myNotes"
    val dbTable="notes"
    val colId="id"
    val colTitle ="title"
    val colDes="description"
    val colDate ="date"
    val dbVersion=1

    // Variable for database table creation.
    val sqlCreateTable ="CREATE TABLE IF NOT EXISTS $dbTable " +
            "($colId INTEGER PRIMARY KEY, $colTitle TEXT, $colDes TEXT, $colDate TEXT);"

    // We create an instant of SQ Lite database.
    var sqlDB:SQLiteDatabase? = null

    constructor(context: Context){
        val dataBase = DataBaseHelperNotes(context)
        sqlDB= dataBase.writableDatabase
    }
    // This Function: DatabaseHelper is responsible for creating database for us.
    inner class DataBaseHelperNotes:SQLiteOpenHelper{
        // We call the constructor and super constructor.
        var context:Context?=null
        constructor(context:Context):super(context, dbName, null, dbVersion){
           this.context=context
        }
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"dataBase is created", Toast.LENGTH_LONG).show()
        }

        // This function is responsible for updating our database.
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
             db!!.execSQL("Drop table IF EXISTS $dbName")
        }
    }

    // This one is responsible for inserting data to database.
    fun insert(values:ContentValues): Long {
        return sqlDB!!.insert(dbTable, "", values)
    }

    //This function will get query from the database.
    fun query(projection: Array<String>, selection: String, selectionArgs:Array<String>, SortOrder: String):Cursor{
        var sqlb = SQLiteQueryBuilder()
        sqlb.tables = dbTable  // Table that I want to get data from
        return sqlb.query(sqlDB, projection, selection, selectionArgs,null, null,SortOrder) // Send some parameters.
    }

    // This function will delete Notes in database
    fun delete(selection: String, selectionArgs: Array<String>):Int{
        return sqlDB!!.delete(dbTable,selection,selectionArgs)
    }

    // This function will update the notes.
    fun update(values:ContentValues, selection: String, selectionArgs: Array<String>): Int{
        return sqlDB!!.update(dbTable,values,selection,selectionArgs)
    }
}