package com.example.noteapp.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.adapter.NoteFirebasAdapter
import com.example.noteapp.model.Model
import com.example.noteapp.model.Note
import com.example.noteapp.viewModel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this, NoteViewModel.NoteViewModelFactory(this.application))[NoteViewModel::class.java]
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var adapter: NoteAdapter
    private lateinit var adapterNoteFirebasAdapter: NoteFirebasAdapter
    private lateinit var btn_add: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var delete: MenuItem
    private lateinit var settings: MenuItem
    private lateinit var dialog: Dialog
    private var arrayList: ArrayList<Model> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar_main))
        btn_add = findViewById(R.id.fab)
        btn_add.setOnClickListener {
            addNote()
        }
        mAuth = FirebaseAuth.getInstance()
        checkLogin()
        initFirebase()
    }

    fun checkLogin() {
        val currenctUser = mAuth.currentUser
        if (currenctUser !== null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("Note-Ap").child("${currenctUser.uid}")
            noteViewModel.getAllNote().observe(this, Observer {
                if (it.isNotEmpty()) {
                    for (item in it) {
                        val key = mDatabase.child("${currenctUser.uid}").push().key
                        noteViewModel.addFirebase(Model(item.container, item.check, item.time, item.title, key.toString()))
                    }
                    noteViewModel.delete(it)
                }


            })
            initFirebase()

        }
    }


    fun setHome(boolean: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(boolean)
    }

    fun initFirebase() {
        adapterNoteFirebasAdapter = NoteFirebasAdapter(this@MainActivity, onLongClickListener, onClickListener)
        recyclerView = findViewById(R.id.recylerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterNoteFirebasAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel.getAllDatafirebase().observe(this, Observer {
            adapterNoteFirebasAdapter.setNoteAdapterFibase(it)
        })

    }

    val onLongClickListener: (Model) -> Unit = {
        settings.isVisible = false
        delete.isVisible = true
        btn_add.visibility = GONE
        setHome(true)
    }
    val onClickListener: (Model) -> Unit = {
        val intent = Intent(this, Update_NoteApp::class.java)
        intent.putExtra("title", it.title.toString())
        intent.putExtra("container", it.container.toString())
        intent.putExtra("time", it.time.toString())
        intent.putExtra("key", it.key)
        startActivity(intent)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        delete = menu?.findItem(R.id.delete)!!
        settings = menu?.findItem(R.id.setting)!!
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.setting -> {
            val intent = Intent(this, Exentend::class.java)
            startActivity(intent)
            true
        }
        R.id.delete -> {
            ShowDialog()
            true
        }
        android.R.id.home -> {

            setHome(false)
            adapter.setHideCheckBox()
            settings.setVisible(true)
            delete.setVisible(false)
            btn_add.visibility = VISIBLE
            true
        }
        else -> {
            super.onContextItemSelected(item)
        }
    }


    fun init() {
        adapter = NoteAdapter(this@MainActivity, LongClick, onClick)
        recyclerView = findViewById(R.id.recylerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteViewModel.getAllNote().observe(this, Observer {
            adapter.setNoteAdapter(it)
        })
    }


    private val LongClick: (Note) -> Unit = {
        settings.isVisible = false
        delete.isVisible = true
        btn_add.visibility = GONE
        setHome(true)
    }
    private val onClick: (Note) -> Unit = {
        val intent = Intent(this, Update_NoteApp::class.java)
        intent.putExtra("title", it.title.toString())
        intent.putExtra("container", it.container.toString())
        intent.putExtra("time", it.time).toString()
        startActivity(intent)
    }

    fun addNote() {
        val intent = Intent(this, Add_NoteApp::class.java)
        startActivity(intent)
    }

    fun Delete() {
        noteViewModel.delete(adapter.getSelected())
        adapter.removeSelect()
        Log.d("lll", adapter.getSelected().toString())
    }

    fun ShowDialog() {
        val currenctUser = mAuth.currentUser
        dialog = Dialog(this)
        dialog.setTitle("delect?")

        var textView: TextView = dialog.findViewById(R.id.how)
        dialog.setContentView(R.layout.delete_dialog)
        if (currenctUser != null) {

        } else {
            textView.setText("Do you want delete ${adapter.getSelected().size} item ?")
            var btn_no: Button = dialog.findViewById(R.id.no)
            var btn_yes: Button = dialog.findViewById(R.id.yes)
            btn_no.setOnClickListener {
                dialog.dismiss()
            }
            btn_yes.setOnClickListener {
                Delete()
                setHome(false)
                adapter.setHideCheckBox()
                settings.setVisible(true)
                delete.setVisible(false)
                btn_add.visibility = VISIBLE
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}