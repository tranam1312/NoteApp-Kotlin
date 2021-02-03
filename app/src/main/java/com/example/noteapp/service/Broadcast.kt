package com.example.noteapp.service

import android.content.BroadcastReceiver
import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
open class  Broadcast():BroadcastReceiver() {
     private var callback :  setNotifi? = null
     constructor(callbacks: setNotifi) : this (){
         this.callback = callbacks
     }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action;
        if (Intent.ACTION_TIME_CHANGED == action || Intent.ACTION_DATE_CHANGED == action){
            callback?.setNotification()
        }
        when(intent?.action){
            Intent.ACTION_TIME_TICK -> callback?.tic()
        }
    }



interface setNotifi{
    fun setNotification()
    fun tic()
}}