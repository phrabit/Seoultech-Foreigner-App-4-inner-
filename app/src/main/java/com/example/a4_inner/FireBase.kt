package com.example.a4_inner

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FireBase {
    val db = Firebase.firestore

    var firebase_online : Boolean? = null

}