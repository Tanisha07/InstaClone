package com.tani.instaclone

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_upload.*
import java.security.AccessController.getContext
import java.util.*
import java.util.jar.Manifest

class UploadAct : AppCompatActivity() {

    var selected : Uri? =null
    var mAuth : FirebaseAuth?=null
    val mAuthListener: FirebaseAuth.AuthStateListener? = null
    var firebaseDatabase : FirebaseDatabase? = null
    var mRef: DatabaseReference? = null
    var storageReference: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        imageView.setImageBitmap(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.img_upload ))
        editText.setText("")
        try {
            mAuth = FirebaseAuth.getInstance()
            firebaseDatabase = FirebaseDatabase.getInstance()
            mRef =firebaseDatabase!!.reference
            storageReference= FirebaseStorage.getInstance().reference
        }
        catch (e : Exception){
            Toast.makeText(applicationContext, e.localizedMessage.toString(), Toast.LENGTH_LONG).show()
        }



    }

    fun cancel(view: View){
        //imageView.setImageBitmap(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.img_upload ))
        val intent = Intent(applicationContext, FeedActivity::class.java)
        //to prevent going back to prev upload activities and going to feed activity instead
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun upload(view: View){
        try {
            val uuid = UUID.randomUUID()
            val imgName = "images/$uuid.jpg"
            val storageRef = storageReference!!.child(imgName)

            storageRef.putFile(selected!!).addOnSuccessListener{ taskSnapshot ->
                var downloadUrl = taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                val user = mAuth!!.currentUser
                val userEmail = user!!.email.toString()
                val userComment = editText.text.toString()
                val uuid = UUID.randomUUID()
                val uuidString = uuid.toString()

                mRef!!.child("Posts").child(uuidString).child("userMail").setValue(userEmail)
                mRef!!.child("Posts").child(uuidString).child("userComment").setValue(userComment)
                mRef!!.child("Posts").child(uuidString).child("downloadURL").setValue(downloadUrl)
                Toast.makeText(applicationContext, "Post Uploaded", Toast.LENGTH_LONG).show()
            }.addOnFailureListener { exception ->
                if (exception != null) {
                    Toast.makeText(
                        applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG
                    ).show()

                }
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Post Shared", Toast.LENGTH_LONG)
                    val intent = Intent(applicationContext, FeedActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        catch (e : Exception){
            e.printStackTrace()
            Toast.makeText(applicationContext, "error 4535", Toast.LENGTH_LONG)
        }
    }

    fun addImg(view: View){
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 2)
        }
        else{
            val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1)
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==2) {
            if( grantResults.isNotEmpty() &&grantResults[0]==PackageManager.PERMISSION_GRANTED )
        {val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,1)}}
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1 && resultCode==Activity.RESULT_OK && data!=null){
            selected = data.data

            try{
                val selectedImg = MediaStore.Images.Media.getBitmap(this.contentResolver, selected)
                imageView.setImageBitmap(selectedImg)
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    }

