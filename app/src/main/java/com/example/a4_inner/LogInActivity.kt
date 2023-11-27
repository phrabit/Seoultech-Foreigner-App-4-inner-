package com.example.a4_inner

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.a4_inner.databinding.ActivityLogInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date


class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    // login variables
//    lateinit var username: EditText
//    lateinit var password: EditText
//    lateinit var loginButton: Button

    // google login
    private val auth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                    Log.d("ITM", "firebaseAuthWithGoogle: " + account.id)
                } catch (e: ApiException) {
                    Log.w("ITM", "Google sign in failed: " + e.message)
                }
            }
        }

    // 페이지 초기화시에 현재 사용자가 로그인 되어있는지 확인
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.goNavi.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
            // Optional: Add any extra information to the intent
            intent.putExtra("fromLogin", "true")
            startActivity(intent) // Add this line to start the activity
            finish()
        }

        binding.loginButton.setOnClickListener {
            if (binding.username.text.toString() == "user" && binding.password.text.toString() == "1234") {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.googleLoginButton.setOnClickListener {
            gSignInFun()
        }
    }

    // 로그인 시 페이지를 변경하는 코드(UI update)
    private fun updateUI(user: FirebaseUser?) { //update ui code here
        if (user != null) {
            userAssign(user)
            val intent = Intent(this, NaviActivity::class.java)
            intent.putExtra("fromLogin", "true")
            startActivity(intent)
            finish()
        }
    }

    private fun gSignInFun() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("ITM", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("ITM", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun userAssign(user: FirebaseUser) {
        // user object initialization(Singleton pattern) + asynchronization
        CurrentUser.initializeUser(user.uid, user.displayName, Timestamp(Date(user.metadata!!.creationTimestamp)), user.email, user.photoUrl)
        // DB write
        // Access a Cloud Firestore instance
        val db = Firebase.firestore
        // check if there's already document for the user
        val userDocRef = db.collection("users").document(user.uid)
        // Check if the document exists
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                // Document does not exist = First time login
                if (!documentSnapshot.exists()) {
                    // making data class for uploading into DB
                    val userData = UserData(
                        user.uid,
                        user.displayName!!,
                        Timestamp(Date(user.metadata!!.creationTimestamp)),
                        user.email!!,
                        user.photoUrl
                    )
                    // Uploading user data for DB
                    db.collection("users").document(user.uid).set(userData)
                        .addOnSuccessListener {
                            Log.d("ITM", "DocumentSnapshot added with ID: ${user.uid}")
                        }
                        .addOnFailureListener { e ->
                            Log.e("ITM", "Error adding document", e)
                        }
                } else {
                    // X First time login -> just simple login
                    Log.d("ITM","User exists")
                }
            }
            .addOnFailureListener { e ->
                Log.e("ITM","error occurred during loading firestore document in userAssign(): $e")
            }

    }
}