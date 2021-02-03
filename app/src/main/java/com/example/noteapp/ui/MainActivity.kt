package com.example.noteapp.ui

import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.adapter.NoteFirebasAdapter
import com.example.noteapp.model.Model
import com.example.noteapp.model.Note
import com.example.noteapp.service.Broadcast
import com.example.noteapp.viewModel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList


class MainActivity() : AppCompatActivity() {
    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this, NoteViewModel.NoteViewModelFactory(this.application))[NoteViewModel::class.java]
    }
    private lateinit var date: Date
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

    }

    fun checkLogin() {
        val currenctUser = mAuth.currentUser
        if (currenctUser !== null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("Note-Ap").child("${currenctUser.uid}")
            noteViewModel.getAllNote().observe(this@MainActivity, androidx.lifecycle.Observer {
                if (it.isNotEmpty()) {
                    for (item in it) {
                        val key = mDatabase.child("${currenctUser.uid}").push().key
                        noteViewModel.addFirebase(Model(item.container, item.check, item.time, item.title, key.toString()))
                    }
                    noteViewModel.delete(it)
                }
            })
            initFirebase()

        }else{
            init()
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

        noteViewModel.getAllDatafirebase().observe(this, androidx.lifecycle.Observer{
            adapterNoteFirebasAdapter.setNoteAdapterFibase(it)
        })

    }

    override fun onStart() {
        super.onStart()
        val  s_intentFilter = IntentFilter()
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK)
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED)
        registerReceiver(callback, s_intentFilter)
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
        noteViewModel.getAllNote().observe(this, androidx.lifecycle.Observer {
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
        intent.putExtra("updatenote", it)
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

    val callback = Broadcast(object : Broadcast.setNotifi {
        override fun setNotification() {
            date = Calendar.getInstance().time
            noteViewModel.getAllNote().observe(this@MainActivity, androidx.lifecycle.Observer {
                for (item in it) {
                    if (item.timeSet.isEmpty() && date.before(item.timeSet as Date)) {
                        if (item.timeSet.equals(date)) {

                        }

                    }
                }
            })
        }

        override fun tic() {
            noteViewModel.getAllNote().observe(this@MainActivity, androidx.lifecycle.Observer {
                for (item in it) {
                    Log.d("ll", "$item")
                    Notification(item)
                }
            })
        }

    })
    fun Notification(note: Note){

        val   builder = NotificationCompat.Builder(this,com.example.noteapp.service.Notification.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_action_notification)
                .setContentTitle(note.title)
                .setContentText(note.container)
                .build()
        val notificationManagerCompat  = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(note.id, builder)
    }


}