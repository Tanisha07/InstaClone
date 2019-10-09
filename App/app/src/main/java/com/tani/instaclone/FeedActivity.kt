package com.tani.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    val emailFromFB: ArrayList<String> = ArrayList()
    val imageFromFB: ArrayList<String> = ArrayList()
    val commentFromFB: ArrayList<String> = ArrayList()
    var firebaseDatabase: FirebaseDatabase? = null
    var myref : DatabaseReference? = null
    var adapter : post_class? = null


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_post, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.add_post){
            val intent = Intent(applicationContext, UploadAct::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        firebaseDatabase = FirebaseDatabase.getInstance()
        myref = firebaseDatabase!!.reference

        adapter = post_class(emailFromFB, imageFromFB, commentFromFB, this)
        listview.adapter = adapter

        getDataFromFB()
    }

    fun getDataFromFB(){
        var newRef = firebaseDatabase!!.getReference("Posts")
        newRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                emailFromFB.clear()
                commentFromFB.clear()
                imageFromFB.clear()
                adapter!!.clear()

                for (snapshot in p0.children){
                    var hashMap = snapshot.value as HashMap<String,String>
                    val username = hashMap["userMail"]
                    val imgurl = hashMap["downloadURL"]
                    val comment = hashMap["userComment"]

                    if(username != null){
                        emailFromFB.add(username)
                    }
                    if (imgurl!=null){
                        imageFromFB.add(imgurl)
                    }
                    if (comment!=null){
                        commentFromFB.add(comment)
                    }
                    adapter!!.notifyDataSetChanged()
                }
            }

        })

    }
}
