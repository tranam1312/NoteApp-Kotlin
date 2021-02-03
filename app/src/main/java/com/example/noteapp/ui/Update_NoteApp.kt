package com.example.noteapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.model.Model
import com.example.noteapp.model.Note
import com.example.noteapp.viewModel.NoteViewModel
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.util.*

class Update_NoteApp : AppCompatActivity() {
    private val noteViewModel:NoteViewModel by lazy {
        ViewModelProvider(this,NoteViewModel.NoteViewModelFactory(this.application))[NoteViewModel::class.java]
    }
    private lateinit var note: Note
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var title: TextView
    private lateinit var container:TextView
    private lateinit var key:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update__note_app)
        setSupportActionBar(findViewById(R.id.toolbar_edit))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = findViewById(R.id.auto_text_update_title)
        container = findViewById(R.id.auto_text_update_container)
        note = intent.getSerializableExtra("updatenote") as Note
        title.text = note.title
        container.text = note.container
        key = intent.getStringExtra("key").toString()

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_edit,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.save_edit->{
            Save()
            true
        }
        R.id.date_time_edit->{
            dateTime(note)
            true
        }
        android.R.id.home->{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            true
        }
        else->{
         super.onOptionsItemSelected(item)
        }
    }
    fun dateTime(note: Note){
        SingleDateAndTimePickerDialog.Builder(this).bottomSheet()
            //.curved()
            .minutesStep(1)

            .displayListener(object : SingleDateAndTimePickerDialog.DisplayListener {
                override fun onDisplayed(picker: SingleDateAndTimePicker) {
                    picker.minDate = Calendar.getInstance().getTime()
                    picker.setDefaultDate(note.timeSet as  Date)
                }

                fun onClosed(picker: SingleDateAndTimePicker?) {

                }
            })
            .title("Chọn ngày và giờ")
            .listener {
               this.note.time = it.toString()
            }.display()
    }
    fun  Save(){
        val mydate: String =
                DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()).toString()
        mAuth = FirebaseAuth.getInstance()
        val  curenUser=mAuth.currentUser
        if (curenUser !=null){
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Note-Ap")
            mDatabaseReference.child("${curenUser.uid}").child("${key}").setValue(Model(container.text.toString(),false,mydate,title.text.toString(),key))
            finish()
        }else{
            note.time = mydate
            note.container =container.text.toString()
            note.title = title.text.toString()
            noteViewModel.updateNote(note)
            finish()
        }
    }
}
