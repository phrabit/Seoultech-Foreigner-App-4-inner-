package com.example.a4_inner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.a4_inner.databinding.ActivityNaviBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


private const val TAG_HOME = "home_fragment"
private const val TAG_BULLETIN = "bulletin_fragment"
private const val TAG_TIMETABLE = "timetable_fragment"
private const val TAG_MAP = "map_fragment"
private const val TAG_AR = "ar_fragment"

class NaviActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNaviBinding

    //TODO 현재 유저
    // variable for GOOGLE login
    private lateinit var auth: FirebaseAuth
    private val user = Firebase.auth.currentUser

    override fun onStart() {
        super.onStart()
        //TODO 현재 유저 정보 받아오기
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "Welcome, ${currentUser.displayName}!", Toast.LENGTH_SHORT).show()
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


        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> setFragment(TAG_HOME, HomeFragment())
                R.id.bulletin -> setFragment(TAG_BULLETIN, BulletinFragment())
                R.id.timetable -> setFragment(TAG_TIMETABLE, TimetableFragment())
                R.id.map -> setFragment(TAG_MAP, MapFragment())
                R.id.ar -> setFragment(TAG_AR, ArFragment())
            }
            true
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
}