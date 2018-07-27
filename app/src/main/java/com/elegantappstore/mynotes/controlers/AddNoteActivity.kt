package com.elegantappstore.mynotes.controlers

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.elegantappstore.mynotes.R
import kotlinx.android.synthetic.main.activity_add_note.*
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val bundle = intent.extras
            if (bundle!= null) {
            id = bundle.getInt("id", 0)

        if (id !=0) {
            addNoteTitle.setText(bundle.getString("title"))
            addNoteDescription.setText(bundle.getString("description"))
        }
            }

    }


     fun addNoteBttnClicked(@Suppress("UNUSED_PARAMETER")view: View) {
        val dbManager = DbManager(this)
        val values = ContentValues()
        values.put("title", addNoteTitle.text.toString())
        values.put("description", addNoteDescription.text.toString())
         val sdf = SimpleDateFormat(" EEE, MMM, d, h:mm a", Locale.getDefault())
         val currentDate = sdf.format(Date())
        values.put("date", currentDate)


        if (id == 0 ) {
            val id = dbManager.insert(values)
            if (id > 0) {
                Toast.makeText(this, R.string.note_is_added, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.can_not_add_note, Toast.LENGTH_LONG).show()
            }
        } else {
            val selectionArgs = arrayOf(id.toString())
            val id = dbManager.update(values, "id=?", selectionArgs )
            if (id > 0) {
                Toast.makeText(this, R.string.note_is_added, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.can_not_add_note, Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }


}
