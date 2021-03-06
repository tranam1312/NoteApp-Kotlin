package com.example.noteapp.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.noteapp.R
import com.example.noteapp.database.responstrory.NoteReponstory
import com.example.noteapp.model.BackGround
import com.example.noteapp.model.Model
import com.example.noteapp.model.Note
import kotlinx.coroutines.launch


class NoteViewModel(app: Application) :ViewModel(){
    private val reponstory:NoteReponstory = NoteReponstory(app)
    fun updateNote(note: Note) = viewModelScope.launch {
        reponstory.updateNote(note)
    }
    fun  delete(notes: List<Note>) = viewModelScope.launch {
        reponstory.DeleteList(notes)
    }
    fun  addNote(note: Note) = viewModelScope.launch {
        reponstory.addNote(note)
    }
     fun getAllDatafirebase():LiveData<MutableList<Model>>{
        val mutableList = MutableLiveData<MutableList<Model>>()
        reponstory.getDataFirebase().observeForever(){
            mutableList.value = it
        }
        return mutableList
    }
    fun addFirebase(model: Model)= viewModelScope.launch {
        reponstory.adDataFireBase(model)
    }
    fun updateFirebase(model: Model)= viewModelScope.launch {
        reponstory.updatefirebase(model)
    }
    fun deleteFirebase(models :List<Model>)= viewModelScope.launch {
        reponstory.deleteFirebas(models)
    }
    fun listBackground():ArrayList<BackGround>{
        val listBackGround:ArrayList<BackGround> = arrayListOf()
        listBackGround.add(BackGround(R.drawable.aanh))
        listBackGround.add(BackGround(R.drawable.anh))
        listBackGround.add(BackGround(R.drawable.ashn))
        listBackGround.add(BackGround(R.drawable.unnamed))
        return listBackGround
    }


    fun getAllNote():LiveData<List<Note>> = reponstory.getAllNote()
    class  NoteViewModelFactory(private val app: Application):ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                return NoteViewModel(app) as T
            }
            throw IllegalArgumentException("loiN")
        }

    }

}
