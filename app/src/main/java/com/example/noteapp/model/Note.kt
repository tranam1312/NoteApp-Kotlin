package com.example.noteapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(@ColumnInfo(name = "title") var title:String="",
                @ColumnInfo(name = "container")var container:String,
                @ColumnInfo(name = "time") var time:String ="", var check:Boolean = false) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}