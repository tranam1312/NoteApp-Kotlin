package com.example.noteapp.database.responstrory

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.database.NoteDatabase
import com.example.noteapp.database.dao.NoteDao
import com.example.noteapp.model.Model
import com.example.noteapp.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NoteReponstory(context:Context) {
    private val noteDao:NoteDao

    private lateinit var mDatabase: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    init {
        val noteDatabase: NoteDatabase = NoteDatabase.getInstance(context)
        noteDao = noteDatabase.getNoteDao()
        mAuth = FirebaseAuth.getInstance()
        val  currenUser = mAuth.currentUser
        if (currenUser != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference("Note-Ap").child("${currenUser.uid}")
        }

    }
    suspend fun addNote(note: Note)= noteDao.addNote(note)
    suspend fun DeleteList(notes: List<Note>) = noteDao.DeleteList(notes)
    suspend fun Delete(note: Note)= noteDao.Delete(note)
    suspend fun updateNote(note: Note) = noteDao.Update(note)
    suspend fun addListNote(notes: List<Note>) = noteDao.addList(notes)
    fun getAllNote():LiveData<List<Note>> =noteDao.getAll()
    fun getDataFirebase():LiveData<MutableList<Model>>{
        val mutableList = MutableLiveData<MutableList<Model>>()
        val listData = mutableListOf<Model>()
        mDatabase.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val model:Model? = snapshot.getValue(Model::class.java)
                if (model != null) {
                    listData.add(model)
                }
                Log.d("lll","${snapshot.getValue(Model::class.java)}")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        mutableList.value = listData
        return mutableList
    }
    fun adDataFireBase(model: Model){
        mDatabase.child("${model.key}").setValue(model)

    }
    fun updatefirebase(model: Model){
        mDatabase.child("${model.key}").setValue(model)
    }
    fun deleteFirebas(models :List<Model>){
        for (item in models){
            mDatabase.child("${item.key}").removeValue()
        }
    }
}