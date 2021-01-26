package com.example.noteapp.ui

import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.R
import com.example.noteapp.model.Model
import com.example.noteapp.model.Note
import com.example.noteapp.viewModel.NoteViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.*

class Update_NoteApp : AppCompatActivity() {
    private val noteViewModel:NoteViewModel by lazy {
        ViewModelProvider(this,NoteViewModel.NoteViewModelFactory(this.application))[NoteViewModel::class.java]
    }
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
        title. text = intent.getStringExtra("title").toString()
        container.text = intent.getStringExtra("container").toString()
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
        android.R.id.home->{
            true
        }
        else->{
         super.onOptionsItemSelected(item)
        }
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
            noteViewModel.updateNote(Note(title.text.toString(),container.text.toString(),mydate))
            finish()
        }



    }
}