package com.tani.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mAuth : FirebaseAuth? =null
    var mAuthLister : FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mAuthLister = FirebaseAuth.AuthStateListener {  }

    }

    fun signIn (view : View){
        mAuth!!.signInWithEmailAndPassword(txtEmail.text.toString(), txtPassword.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(applicationContext, FeedActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener { exception ->
                if (exception!=null){
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    fun signUp (view: View){
        mAuth!!.createUserWithEmailAndPassword(txtEmail.text.toString(), txtPassword.text.toString())
            .addOnCompleteListener { task ->
                 if(task.isSuccessful){
                    Toast.makeText(applicationContext,"User Created", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, FeedActivity::class.java)
                startActivity(intent)}
            }.addOnFailureListener { exception ->
                if (exception!=null){
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}
