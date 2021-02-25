package com.example.noteapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.adapter.AdapterBackground
import com.example.noteapp.model.BackGround
import com.example.noteapp.model.Model
import com.example.noteapp.model.Note
import com.example.noteapp.viewModel.NoteViewModel
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.util.*


class Add_NoteApp : AppCompatActivity() {
    private val noteViewModel:NoteViewModel by lazy {
        ViewModelProvider(this, NoteViewModel.NoteViewModelFactory(application))[NoteViewModel::class.java]
    }
    private  var date: Date?= null
    private lateinit var menu_setTime:LinearLayout
    private lateinit var backGroundAdd: ConstraintLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var backGround:BackGround
    private lateinit var set_reminder:TextView
    private lateinit var adapterBackGround:AdapterBackground
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomSheetDialog :BottomSheetDialog
    private lateinit var txtTitle:EditText
    private lateinit var txtContainer:AutoCompleteTextView
    private lateinit var noticeLevel: LinearLayout
    private lateinit var level:TextView
    private lateinit var notificationRingtone:LinearLayout
    private lateinit var ring:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__note_app)
        txtTitle = findViewById(R.id.auto_title)
        txtContainer = findViewById(R.id.auto_text_container)
        backGroundAdd = findViewById(R.id.layout_add)
        menu_setTime = findViewById(R.id.set_time)
        set_reminder = findViewById(R.id.set_time_reminder)
        noticeLevel = findViewById(R.id.notiec_level)
        level = findViewById(R.id.level)
        notificationRingtone = findViewById(R.id.notification_ringtones)
        ring = findViewById(R.id.ring)
        setSupportActionBar(findViewById(R.id.toolbar_add))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        onClickSetReminder()
        onClickNoticeLevel()
        onClickSeclectAlarm()
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
        android.R.id.home -> {
          finish()
            true
        }
        else->{
         super.onOptionsItemSelected(item)
        }
    }
    private fun onClickSetReminder(){
        menu_setTime.setOnClickListener {
            if (set_reminder.text !="Off"){
                showMenuSetTime()
            }else{
                dateTime()
            }
        }
    }
    private fun showMenuSetTime(){
        val popupMenu= PopupMenu(this,set_reminder)
        popupMenu.menuInflater.inflate(R.menu.menu_set_time,popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener{
            onClickItemMenuSetReminderTime(it)
        }
    }
    private  fun onClickItemMenuSetReminderTime(item: MenuItem):Boolean = when(item.itemId){
        R.id.set_time_reminder->{
            dateTime()
            true
        }
        R.id.off_time ->{
            set_reminder.text = "Off"
            true
        }
        else -> false
    }
    private fun dateTime() {
        SingleDateAndTimePickerDialog.Builder(this).bottomSheet()
                //.curved()
            .minutesStep(1)

                .displayListener(object : SingleDateAndTimePickerDialog.DisplayListener {
                    override fun onDisplayed(picker: SingleDateAndTimePicker) {
                        // Retrieve the SingleDateAndTimePicker
                    }

                    fun onClosed(picker: SingleDateAndTimePicker?) {
                        // On dialog closed
                    }
                })
                .title("Chọn ngày và giờ")
                .listener { date  = it
                    set_reminder.text = date.toString()
                    Log.d("kk","$date") }.display()
    }
    private fun onClickNoticeLevel(){
        noticeLevel.setOnClickListener {
            showMenuNoticeLevel()
        }
    }
    private fun showMenuNoticeLevel(){
        val popupMenu = PopupMenu(this, level)
        popupMenu.menuInflater.inflate(R.menu.menu_notice_level, popupMenu.menu)
        when(level.text){
            "Hight" ->{
                val hight: MenuItem = popupMenu.menu.findItem(R.id.hight)
                hight.isVisible = false
            }
            "Medium"->{
                val medium:MenuItem = popupMenu.menu.findItem(R.id.medium)
                medium.isVisible = false
            }
            "Low"->{
                val low:MenuItem = popupMenu.menu.findItem(R.id.low)
                low.isVisible = false
            }
            "Default" ->{
                val off:MenuItem = popupMenu.menu.findItem(R.id.default_notice)
                off.isVisible = false
            }
        }
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {
            onClickItemMenuNoticeLevel(it)
        }
    }
    private  fun onClickItemMenuNoticeLevel(item: MenuItem):Boolean = when(item.itemId){
        R.id.hight->{
            level.text = "Hight"
            true
        }
        R.id.medium->{
            level.text = "Medium"
            true
        }
        R.id.low ->{
            level.text = "Low"
            true
        }
        R.id.default_notice ->{
            level.text="Default"
            true
        }
        else -> false
    }
    private fun onClickSeclectAlarm(){
        notificationRingtone.setOnClickListener {
            val intent = Intent(this, NotificationRingtonesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun Save(){
        val mydate: String = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()).toString()
        mAuth = FirebaseAuth.getInstance()
        val currenctUser = mAuth.currentUser
        if (currenctUser !==null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("Note-Ap")
            val key  =   mDatabase.child("${currenctUser.uid}").push().key
            mDatabase.child("${currenctUser.uid}").child("$key").setValue(
                    Model(txtContainer.text.toString(), false, mydate, txtTitle.text.toString(), key.toString()))
            finish()
        }else {
            noteViewModel.addNote(
                    Note(txtTitle.text.toString(), txtContainer.text.toString(), mydate, 0, date.toString())
            )
            finish()
        }
    }

    fun setBackground(){
        bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.backgound_bottom_sheet, null)
        bottomSheetDialog.setContentView(view)
        initsetBackgound(view)
        bottomSheetDialog.show()
    }
    fun initsetBackgound(view: View){
        adapterBackGround = AdapterBackground(applicationContext, setClickBackground)
        recyclerView = view.findViewById(R.id.bottom_sheet_background)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapterBackGround
        adapterBackGround.setArrImgBack(noteViewModel.listBackground())
    }
    val setClickBackground : (BackGround) ->Unit={
        backGroundAdd.setBackgroundResource(it.recosun)
//        backGround.recosun = it.recosun
    }


}

