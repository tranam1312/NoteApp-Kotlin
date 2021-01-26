package com.example.noteapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


class Add_NoteApp : AppCompatActivity() {
    private val noteViewModel:NoteViewModel by lazy {
        ViewModelProvider(this, NoteViewModel.NoteViewModelFactory(application))[NoteViewModel::class.java]
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var date_time:String
    private  var mYear:Int = 0
    private  var mDay :Int = 0
    private var mMonth:Int =0
    private var mHour:Int=0
    private var mMinute:Int=0
    private lateinit var txtTitle:AutoCompleteTextView
    private lateinit var txtContainer:AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__note_app)
        setSupportActionBar(findViewById(R.id.toolbar_add))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        txtTitle = findViewById(R.id.auto_title)
        txtContainer = findViewById(R.id.auto_text_container)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_save, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.save -> {
            Save()
            true
        }
        R.id.date_time -> {
            dateTime()
            true
        }
        android.R.id.home -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true
        }
        else->{
         super.onOptionsItemSelected(item)
        }
    }

    private fun dateTime() {
        SingleDateAndTimePickerDialog.Builder(this).bottomSheet()
                .title("chọn ngày và giờ ")
                .setTimeZone(TimeZone.getDefault())
                .displayListener(object : SingleDateAndTimePickerDialog.DisplayListener {
                    override fun onDisplayed(picker: SingleDateAndTimePicker) {

                    }

                    fun onClosed(picker: SingleDateAndTimePicker?) {
                        // On dialog closed
                    }
                })

                .listener {
                    SingleDateAndTimePickerDialog.Listener { date ->
                        date_time = date?.time.toString()
                        Toast.makeText(applicationContext,"${date_time}",Toast.LENGTH_SHORT).show()
                    }
                }.display()
    }

    private fun Save(){
        val mydate: String =
                DateFormat.getDateTimeInstance()
                        .format(Calendar.getInstance().getTime()).toString()
        mAuth = FirebaseAuth.getInstance()
        val currenctUser = mAuth.currentUser
        if (currenctUser !==null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("Note-Ap")
            val key  =   mDatabase.child("${currenctUser.uid}").push().key
            mDatabase.child("${currenctUser.uid}").child("$key").setValue(Model( txtContainer.text.toString(),false,mydate,txtTitle.text.toString(),key.toString()))
            finish()
        }else{

             noteViewModel.addNote(Note(txtTitle.text.toString(), txtContainer.text.toString(), mydate))
             finish()
    }
    }


}