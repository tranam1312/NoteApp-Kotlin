package com.example.noteapp.model


data class Model(var container :String ,var check:Boolean,var time:String,var title: String,var key:String="") {
    constructor(): this("",
            false,
            "",
            "","")
}