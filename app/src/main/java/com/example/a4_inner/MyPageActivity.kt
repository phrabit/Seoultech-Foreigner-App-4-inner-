package com.example.a4_inner

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.a4_inner.databinding.ActivityMyPageBinding
import com.example.a4_inner.databinding.ActivityNaviBinding
import com.example.a4_inner.databinding.FragmentHomeBinding
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MyPageActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //  Assuming you have the user's photo URI stored in a variable named photoUri
        val photoUrl: Uri? = CurrentUser.getPhotoUrl

        // Reference to the ImageView
        val userPhotoImageView: ImageView = binding.userPhotoImageView2
        val userNameText: TextView = binding.textView3

        userNameText.text = CurrentUser.getName

        // Load the user's photo into the ImageView using Glide
        Glide.with(this)
            .load(photoUrl)
            .placeholder(R.drawable.user) // Placeholder image while loading
            .error(R.drawable.user) // Error image if loading fails
            .circleCrop()
            .into(userPhotoImageView)

        binding.logOutButton2.setOnClickListener {
            // sign out
            try {
                Firebase.auth.signOut()
                Log.d("ITM","successfully signed out")
            } catch (e: ApiException) {
                Log.d("ITM","Sign out failed")
            }
            val intent = Intent(this, LogInActivity::class.java);
            startActivity(intent)
        }
    }
}