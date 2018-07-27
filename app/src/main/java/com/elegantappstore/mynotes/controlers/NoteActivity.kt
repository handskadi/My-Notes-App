package com.elegantappstore.mynotes.controlers

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.elegantappstore.mynotes.R
import kotlinx.android.synthetic.main.activity_note.*


class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val bundle = intent.extras
        noteNoteDate.text = bundle.getString("noteDate")
        noteNoteTitle.text = bundle.getString("noteTitle")
        noteNoteDescription.text = bundle.getString("noteDescription")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

     override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){

            R.id.noteMenuAddNote -> {
                val intent = Intent(this, AddNoteActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.noteDelete ->{
                lateinit var dialog:AlertDialog
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Delete Note?")
                val noteDialogText = noteNoteTitle.text
                builder.setMessage("Are you sure you want to delete \"$noteDialogText\"?")
                val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
                    when(which){
                        DialogInterface.BUTTON_POSITIVE -> {
                            val bundle = intent.extras
                            val noteId = bundle.getInt("noteId")
                            val dbManager = DbManager(this@NoteActivity)
                            val selectionArgs = arrayOf(noteId.toString())
                            dbManager.delete("id=?",selectionArgs  )
                            Toast.makeText(this,"Note is Deleted",Toast.LENGTH_LONG).show()
                            finish()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            Toast.makeText(this, "Delete is cancelled", Toast.LENGTH_SHORT).show() }
                    }
                }
                builder.setPositiveButton("DELETE",dialogClickListener)
                builder.setNegativeButton("CANCEL",dialogClickListener)
                dialog = builder.create()
                dialog.show()
            }

            R.id.noteEdit -> {
                val bundle = intent.extras

                val myId = bundle.getInt("noteId")
                val intent = Intent(this, AddNoteActivity::class.java)
                intent.putExtra("id", myId)
                intent.putExtra("description", noteNoteDescription.text)
                intent.putExtra("title",noteNoteTitle.text)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
