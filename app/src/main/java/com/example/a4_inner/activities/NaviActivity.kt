package com.example.a4_inner.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.a4_inner.CurrentUser
import com.example.a4_inner.FireBase
import com.example.a4_inner.FragmentTags
import com.example.a4_inner.R
import com.example.a4_inner.fragments.TimetableFragment
import com.example.a4_inner.databinding.ActivityNaviBinding
import com.example.a4_inner.fragments.ArFragment
import com.example.a4_inner.fragments.BulletinFragment
import com.example.a4_inner.fragments.HomeFragment
import com.example.a4_inner.fragments.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class NaviActivity : AppCompatActivity() {

    public lateinit var binding: ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragment(FragmentTags.TAG_HOME, HomeFragment())

        binding.cameraBtn.setOnClickListener{
            val packageName = "com.google.android.apps.translate"
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                startActivity(intent)
            } else {
                val playStoreUri = Uri.parse("market://details?id=$packageName")
                val goToPlayStore = Intent(Intent.ACTION_VIEW, playStoreUri)
                if (goToPlayStore.resolveActivity(packageManager) != null) {
                    startActivity(goToPlayStore)
                }
            }
        }

        binding.myPageBtn.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    setFragment(FragmentTags.TAG_HOME, HomeFragment())
                    val fragment = supportFragmentManager.findFragmentByTag(FragmentTags.TAG_HOME)
                    if (fragment is HomeFragment) {
                        fragment.refresh()
                    }
                }
                R.id.bulletin -> setFragment(FragmentTags.TAG_BULLETIN, BulletinFragment())
                R.id.timetable -> setFragment(FragmentTags.TAG_TIMETABLE, TimetableFragment())
                R.id.map -> setFragment(FragmentTags.TAG_MAP, MapFragment())
                R.id.ar -> setFragment(FragmentTags.TAG_AR, ArFragment())
            }
            true
        }

        if (intent.getStringExtra("fromLogin") == "true") {
            val userAuth = Firebase.auth.currentUser
            if (userAuth != null) {
                Toast.makeText(this, "Welcome, ${CurrentUser.getName}!", Toast.LENGTH_SHORT).show()
                intent.removeExtra("fromLogin")
                val docRef = FireBase.db.collection("users").document(CurrentUser.getUserUid!!)
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val department = document.getString("department")
                            val stuNum = document.getString("stuNumber")
                            val grade = document.getString("grade")
                            val nation = document.getString("nation")

                            if (department == null || stuNum == null || grade == null || nation == null) {
                                showDialog()
                            }
                        } else {
                            Log.d("ITM", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("ITM", "get failed with ", exception)
                    }
            } else {
                val intent = Intent(this, LogInActivity::class.java)

                Log.d("ITM","Fuck OFF")
                Log.d("ITM", "fromLogin: ${intent.getStringExtra("fromLogin")}")
                Log.d("ITM", "Firebase.auth.currentUser: ${Firebase.auth.currentUser}")


                startActivity(intent)
                finish()
            }
        }
        // 수호 로그인일때 허용.
        else if (intent.getStringExtra("fromLogin") == "suho"){
            intent.removeExtra("fromLogin")
            Log.d("ITM","Current userUid = ${CurrentUser.getUserUid}")
        }
    }

    public fun setFragment(tag:String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(FragmentTags.TAG_HOME)
        val bulletin = manager.findFragmentByTag(FragmentTags.TAG_BULLETIN)
        val timetable = manager.findFragmentByTag(FragmentTags.TAG_TIMETABLE)
        val map = manager.findFragmentByTag(FragmentTags.TAG_MAP)
        val ar = manager.findFragmentByTag(FragmentTags.TAG_AR)

        val navigationView = findViewById<BottomNavigationView>(R.id.navigationView)
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

        if (tag == FragmentTags.TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
            navigationView.menu[0].setChecked(true)
        } else if (tag == FragmentTags.TAG_BULLETIN) {
            if (bulletin != null) {
                fragTransaction.show(bulletin)
            }
            navigationView.menu[1].setChecked(true)
        } else if (tag == FragmentTags.TAG_TIMETABLE) {
            if (timetable != null) {
                fragTransaction.show(timetable)
            }
            navigationView.menu[2].setChecked(true)
        } else if (tag == FragmentTags.TAG_MAP) {
            if (map != null) {
                fragTransaction.show(map)
            }
            navigationView.menu[3].setChecked(true)
        } else if (tag == FragmentTags.TAG_AR) {
            if (ar != null) {
                fragTransaction.show(ar)
            }
            navigationView.menu[4].setChecked(true)
        }
        fragTransaction.commitAllowingStateLoss()

    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Additional Information needed!")
        builder.setMessage("There are some information missing. Please fill in!")

        builder.setPositiveButton("Confirm") { _, _ ->
            val intent = Intent(this, MyPageActivity::class.java)
            // Start the LoginActivity
            startActivity(intent)
            finish()
        }

        val dialog = builder.create()
        dialog.show()
    }
}

