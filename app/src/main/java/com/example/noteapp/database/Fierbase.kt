//package com.example.noteapp.database
//
//import android.content.Context
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.room.Room
//import com.example.noteapp.model.Model
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//
//open class Fierbase {
//
//    private lateinit var mDatabase: DatabaseReference
//    private lateinit var mAuth: FirebaseAuth
//
//companion object{
//    private var intrance : Fierbase? = null
//    fun  getInstance() :Fierbase {
//        if (intrance == null) {
//            intrance =  Fierbase()
//        }
//        return intrance!!
//    }
//}
//
//
//    fun getDataFirebase(): MutableLiveData<MutableList<Model>> {
//        mAuth = FirebaseAuth.getInstance()
//        val  currenUser = mAuth.currentUser
//        if (currenUser != null) {
//            mDatabase = FirebaseDatabase.getInstance().getReference("${currenUser.uid}")
//            val mutableList = MutableLiveData<MutableList<Model>>()
//            val listData = mutableListOf<Model>()
//            mDatabase.addChildEventListener(object : ChildEventListener {
//                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    val model: Model? = snapshot.getValue(Model::class.java)
//                    if (model != null) {
//                        listData.add(model)
//                    }
//                    Log.d("lll", "${snapshot.getValue(Model::class.java)}")
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onChildRemoved(snapshot: DataSnapshot) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//
//            mutableList.value = listData
//            return mutableList
//        }
//
//    fun adDataFireBase(model: Model){
//        mAuth = FirebaseAuth.getInstance()
//        val  currenUser = mAuth.currentUser
//        if (currenUser != null) {
//            mDatabase = FirebaseDatabase.getInstance().getReference("${currenUser.uid}")
//            mDatabase.child("${model.key}").setValue(model)
//        }
//    }
//    fun updatefirebase(model: Model){
//        mAuth = FirebaseAuth.getInstance()
//        val  currenUser = mAuth.currentUser
//        if (currenUser != null) {
//            mDatabase = FirebaseDatabase.getInstance().getReference("${currenUser.uid}")
//            mDatabase.child("${model.key}").setValue(model)
//        }
//    }
//    fun deleteFirebas(models :List<Model>){
//        mAuth = FirebaseAuth.getInstance()
//        val  currenUser = mAuth.currentUser
//        if (currenUser != null) {
//            mDatabase = FirebaseDatabase.getInstance().getReference("${currenUser.uid}")
//            for (item in models) {
//                mDatabase.child("${item.key}").removeValue()
//            }
//        }
//    }
//
//}