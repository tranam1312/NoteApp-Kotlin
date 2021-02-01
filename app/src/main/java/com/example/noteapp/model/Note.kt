package com.example.noteapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note_table")
data class Note(@ColumnInfo(name = "title") var title:String="",
                @ColumnInfo(name = "container")var container:String,
                @ColumnInfo(name = "time") var time:String ="",@ColumnInfo(name = "background")var backGround: Int =0 ,@ColumnInfo(name = "timeset")var timeSet:String="", var check:Boolean = false):Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}