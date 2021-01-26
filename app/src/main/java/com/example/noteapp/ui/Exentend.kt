package com.example.noteapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.noteapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class Exentend : AppCompatActivity() {
    private  lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btn_signIn: SignInButton
    private lateinit var imgView_google: ImageView
    private lateinit var email_google: TextView
    private lateinit var btn_sigout_google: Button
    private lateinit var synchronize : Switch
    private lateinit var gird_view:Switch
    private var ischeck_synchronize:Boolean = false
    private var ischeck_gird_view:Boolean=  false
    companion object{
        private const val RC_SIGN_IN =120
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exentend)
        btn_signIn = findViewById(R.id.sign_in_google)
        imgView_google = findViewById(R.id.img_google)
        email_google = findViewById(R.id.email)
        btn_sigout_google = findViewById(R.id.btn_sign_out_google)
        synchronize = findViewById(R.id.synchronize)
        gird_view = findViewById(R.id.gird_view)
        btn_sigout_google.visibility = View.GONE
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("53390066358-m6e0vtevgqn0um8vvivia0ot2v83eo3k.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        mAuth = FirebaseAuth.getInstance()
        btn_signIn.setOnClickListener {
            signIn()
        }
       ischeck_gird_view = gird_view.isChecked
        ischeck_synchronize = synchronize.isChecked
        btn_sigout_google.setOnClickListener { signOut() }

    }


    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user != null) {
            Log.d("kkk","${user}")
            btn_signIn.visibility = View.GONE
            Glide.with(this).load(user.photoUrl).into(imgView_google)
            email_google.text = user.email
            imgView_google.visibility= View.VISIBLE
            email_google.visibility = View.VISIBLE
            btn_sigout_google.visibility = View.VISIBLE
            ischeck_synchronize = synchronize.isChecked
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("Sign in", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.d("Sign in", "Google sign in failed", e)
                    // ...
                }
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sign in success", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    if (user != null) {
                        Log.d("kkk","${user}")
                        btn_signIn.visibility = View.GONE
                        Glide.with(this).load(user.photoUrl).into(imgView_google)
                        email_google.text = user.email
                        btn_sigout_google.visibility = View.VISIBLE
                    }

                } else {

                    Log.d("sign in", "signInWithCredential:failure", task.exception)

                }

            }
    }
    fun signOut(){
        FirebaseAuth.getInstance().signOut();
        imgView_google.visibility =View.GONE
        email_google.visibility = View.GONE
        btn_sigout_google.visibility = View.GONE
        btn_signIn.visibility =View.VISIBLE
    }

}