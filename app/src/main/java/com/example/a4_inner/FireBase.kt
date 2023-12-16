package com.example.a4_inner

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FireBase {
    val db = Firebase.firestore

    var firebase_online : Boolean? = null

}