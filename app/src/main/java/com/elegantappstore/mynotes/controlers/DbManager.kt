package com.elegantappstore.mynotes.controlers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager(context: Context) {
    // Define variables to create database:
    val dbName ="myNotes"
    private val dbTable="notes"
    private val colId="id"
    private val colTitle ="title"
    private val colDes="description"
    private val colDate ="date"
    val dbVersion=1

    // Variable for database table creation.
    val sqlCreateTable ="CREATE TABLE IF NOT EXISTS $dbTable " +
            "($colId INTEGER PRIMARY KEY, $colTitle TEXT, $colDes TEXT, $colDate TEXT);"

    // We create an instant of SQ Lite database.
    private var sqlDB:SQLiteDatabase? = null

    init {
        val dataBase = DataBaseHelperNotes(context)
        sqlDB= dataBase.writableDatabase
    }
    // This Function: DatabaseHelper is responsible for creating database for us.
    inner class DataBaseHelperNotes(context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
        // We call the constructor and super constructor.
        private var context:Context?= context

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
        val sqlb = SQLiteQueryBuilder()
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