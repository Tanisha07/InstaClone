package com.tani.instaclone

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_view.view.*

class post_class (private val userEmail: ArrayList<String>, private val userImg : ArrayList<String>, private val userComment: ArrayList<String>, private val context : Activity ) : ArrayAdapter<String>(context, R.layout.custom_view, userEmail){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = context.layoutInflater
        val customView = layoutInflater.inflate(R.layout.custom_view, null, true)
        customView.customEmail.setText(userEmail[position])
        customView.customComment.setText(userComment[position])

        Picasso.get().load(userImg[position]).resize(300,293).centerCrop().into(customView.customImage)

        return customView
    }
}