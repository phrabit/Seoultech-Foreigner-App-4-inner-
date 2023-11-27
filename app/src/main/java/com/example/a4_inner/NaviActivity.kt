package com.example.a4_inner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.a4_inner.databinding.ActivityNaviBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore


private const val TAG_HOME = "home_fragment"
private const val TAG_BULLETIN = "bulletin_fragment"
private const val TAG_TIMETABLE = "timetable_fragment"
private const val TAG_MAP = "map_fragment"
private const val TAG_AR = "ar_fragment"

class NaviActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNaviBinding

    // variable for GOOGLE login
//    private lateinit var auth: FirebaseAuth

    // variable for user data
//    private lateinit var userDocRef: DocumentReference
//    private lateinit var snapshotListener: ListenerRegistration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> setFragment(TAG_HOME, HomeFragment())
                R.id.bulletin -> setFragment(TAG_BULLETIN, BulletinFragment())
                R.id.timetable -> setFragment(TAG_TIMETABLE, TimetableFragment())
                R.id.map -> setFragment(TAG_MAP, MapFragment())
                R.id.ar -> setFragment(TAG_AR, ArFragment())
            }
            true
        }

        // User verification
        // 만약 로그인 액티비티에서 넘어왔다면
        if (intent.getStringExtra("fromLogin") == "true") {
            val userAuth = Firebase.auth.currentUser
            if (userAuth != null) {
                Toast.makeText(this, "Welcome, ${CurrentUser.getName}!", Toast.LENGTH_SHORT).show()
                intent.removeExtra("fromLogin")
                Log.d("ITM","PhotoUrl: ${CurrentUser.getPhotoUrl}")
            } else {
                // If user tries to access Navi Activity with no auth, directly navigate to LogInActivity

                // Create an Intent to start the LoginActivity
                val intent = Intent(this, LogInActivity::class.java)

                // Optional: Add any extra information to the intent
                // intent.putExtra("key", "value")

                // Start the LoginActivity
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val bulletin = manager.findFragmentByTag(TAG_BULLETIN)
        val timetable = manager.findFragmentByTag(TAG_TIMETABLE)
        val map = manager.findFragmentByTag(TAG_MAP)
        val ar = manager.findFragmentByTag(TAG_AR)


        if (home != null) {
            fragTransaction.hide(home)
        }

        if (bulletin != null) {
            fragTransaction.hide(bulletin)
        }

        if (timetable != null) {
            fragTransaction.hide(timetable)
        }

        if (map != null) {
            fragTransaction.hide(map)
        }

        if (ar != null) {
            fragTransaction.hide(ar)
        }

        if (tag == TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
        } else if (tag == TAG_BULLETIN) {
            if (bulletin != null) {
                fragTransaction.show(bulletin)
            }
        } else if (tag == TAG_TIMETABLE) {
            if (timetable != null) {
                fragTransaction.show(timetable)
            }
        } else if (tag == TAG_MAP) {
            if (map != null) {
                fragTransaction.show(map)
            }
        } else if (tag == TAG_AR) {
            if (ar != null) {
                fragTransaction.show(ar)
            }
        }
        fragTransaction.commitAllowingStateLoss()
    }

//    private fun setupFirestoreListener() {
//        snapshotListener = userDocRef.addSnapshotListener { snapshot, e ->
//            if (e != null) {
//                Log.e("ITM", "Firestore listen failed.", e)
//                return@addSnapshotListener
//            }
//
//            // Check if the changes are from local writes
//            val source = if (snapshot != null && snapshot.metadata.hasPendingWrites()) {
//                "Local"
//            } else {
//                "Server"
//            }
//
//            // Log the data based on whether it's a local or server change
//            if (snapshot != null && snapshot.exists()) {
//                //TODO userData 밖에 구현해서 활용(homeFragment instance로 전달)
//
//                val userData = snapshot.toObject(UserData::class.java)
//
//                userData?.let {
//                    Log.d("ITM", "$source data: UserUid=${it.userUid}, Name=${it.name}, CreationDate=${it.creationDate}")
//                    // User data transmission(각 fragment 별로 구현해놓기)
////                    HomeFragment.newInstance(it)
//                }
//            } else {
//                Log.d("ITM", "$source data: null")
//            }
//        }
//    }
}

