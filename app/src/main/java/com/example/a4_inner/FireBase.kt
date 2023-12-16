package com.example.a4_inner

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FireBase {
    @SuppressLint("StaticFieldLeak")
    val db = Firebase.firestore

    var firebase_online : Boolean? = null

}