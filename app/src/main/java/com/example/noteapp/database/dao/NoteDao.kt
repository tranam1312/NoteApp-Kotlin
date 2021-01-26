package com.example.noteapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noteapp.model.Note

@Dao
interface NoteDao {
    @Update
    suspend fun Update(note: Note)
    @Insert
    suspend fun addNote(note: Note)
    @Insert
    suspend fun addList(notes: List<Note>)
    @Delete
    suspend fun DeleteList(note: List<Note>)
    @Delete
    suspend fun Delete(note: Note)
    @Query("SELECT * FROM note_table")
     fun getAll(): LiveData<List<Note>>
}