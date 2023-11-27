package com.example.a4_inner

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.parcelize.Parcelize
import java.util.Date

object FirestoreProvider {
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}

object CurrentUser {
    private var _userUid: String? = null
    private var _name: String? = null
    private var _creationDate: Timestamp? = null
    private var _email: String? = null
    private var _photoUrl: Uri? = null

    // custom getters
    val getUserUid: String?
        get() = _userUid
    val getName: String?
        get() = _name
    val getCreationDate: Timestamp?
        get() = _creationDate
    val getEmail: String?
        get() = _email
    val getPhotoUrl: Uri?
        get() = _photoUrl

    private lateinit var userRef: DocumentReference

    fun initializeUser(uid: String, name: String?, creationDate: Timestamp, email: String?, photoUrl: Uri?) {
        this._userUid = uid
        this._name = name
        this._creationDate = creationDate
        this._email = email
        this._photoUrl = photoUrl
        userRef = FirestoreProvider.db.collection("users").document(uid)

        userRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("ITM", "Listen failed.", e)
                return@addSnapshotListener
            }

            val source = if (snapshot != null && snapshot.metadata.hasPendingWrites()) {
                "Local"
            } else {
                "Server"
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("ITM", "$source data: ${snapshot.data}")
                updateUserData(snapshot) // 데이터 변경시 사용자 데이터 업데이트
            } else {
                Log.d("ITM", "$source data: null")
            }
        }
    }

    private fun updateUserData(snapshot: DocumentSnapshot) {
        _userUid = snapshot.getString("userUid")
        _name = snapshot.getString("name")
        _creationDate = snapshot.getTimestamp("creationDate")
        _email = snapshot.getString("email")
        val photoUrlStr = snapshot.getString("photoUrl")
        if (photoUrlStr != null) {
            _photoUrl = Uri.parse(photoUrlStr)
        } else {
            _photoUrl = null
        }
    }
}

data class UserData(
    val userUid: String?,
    val name: String?, // Firebase.auth.currentUser.displayName
    val creationDate: Timestamp?, // Firebase.auth.currentUser.metadata.creationTime
    val email: String?,
    val photoUrl: Uri?
)
