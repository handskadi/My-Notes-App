package com.elegantappstore.mynotes.Controlers

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import com.elegantappstore.mynotes.Models.Note
import com.elegantappstore.mynotes.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.note_ticket.view.*

class MainActivity : AppCompatActivity() {
    var listOfNotes = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadQuery("%")
    }

     override fun onResume() {
        super.onResume()
         loadQuery("%")
    }

    // Load Function: // Load from the database
    fun loadQuery(title:String){
        val dbManager = DbManager(this)
        val projection = arrayOf("id","title","description","date")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projection,"title like ?",selectionArgs, "title" )
        listOfNotes.clear()
        if (cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val description  = cursor.getString(cursor.getColumnIndex("description"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                listOfNotes.add(Note(id, title, description, date))

            }while (cursor.moveToNext())
        }
        val adapter= MyNotesAdapter(listOfNotes)
        mainListView.adapter = adapter
    }

// ============== How to implement Search functionaries =============

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setQueryHint("Search Now...")
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQuery("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadQuery("%$newText%")
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.mainMenuAddNote ->{
                val intent = Intent(this,AddNoteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // End of Search View

    inner class MyNotesAdapter: BaseAdapter {
        var listOfNotesAdapter = ArrayList<Note>()
        constructor(listOfNotesAdapter:ArrayList<Note>):super(){
            this.listOfNotesAdapter=listOfNotesAdapter
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.note_ticket,null)
            var myNote = listOfNotesAdapter[position]
            myView.mainNoteTitle.text = myNote.noteTitle

            if (myNote.noteDescription?.length!! >=30){
                        myView.mainNoteDescription.text = "${myNote.noteDescription?.substring(0, 30)}..."
                    }else {
                        myView.mainNoteDescription.text = "${myNote.noteDescription}..."
                    }
            myView.mainNoteDate.text = myNote.noteDate


            // ---- Delete Note in the list view.
            myView.mainDelete.setOnClickListener ( View.OnClickListener{
                var dbManager = DbManager(this@MainActivity)
                val selectionArgs = arrayOf(myNote.id.toString())
                dbManager.delete("id=?" ,selectionArgs)
                loadQuery("%")
            })

            // ---- Go to other activity of Adding new note..
            myView.emptyLayer.setOnClickListener {
                val intent = Intent(this@MainActivity, NoteActivity::class.java)
                intent.putExtra("noteTitle", myNote.noteTitle)
                intent.putExtra("noteDescription", myNote.noteDescription)
                intent.putExtra("noteDate", myNote.noteDate)
                intent.putExtra("noteId", myNote.id)
                startActivity(intent)
            }
            myView.emptyLayer2.setOnClickListener {
                val intent = Intent(this@MainActivity, NoteActivity::class.java)
                intent.putExtra("noteTitle", myNote.noteTitle)
                intent.putExtra("noteDescription", myNote.noteDescription)
                intent.putExtra("noteDate", myNote.noteDate)
                intent.putExtra("noteId", myNote.id)
                startActivity(intent)
            }

            // update Button
            myView.mainUpdate.setOnClickListener( View.OnClickListener{
                goToUpdate(myNote)
            })

            return myView
        }
        override fun getItem(position: Int): Any {
            return listOfNotesAdapter[position]
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getCount(): Int {
            return listOfNotesAdapter.size
        }
    }
    fun goToUpdate(note: Note){
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("id", note.id)
        intent.putExtra("description", note.noteDescription)
        intent.putExtra("title",note.noteTitle)
        startActivity(intent)
    }
}
