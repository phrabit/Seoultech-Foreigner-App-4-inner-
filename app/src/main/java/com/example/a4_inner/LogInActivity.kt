package com.example.a4_inner

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.a4_inner.databinding.ActivityLogInBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    // login variables
//    lateinit var username: EditText
//    lateinit var password: EditText
//    lateinit var loginButton: Button

    // google login
    private lateinit var auth: FirebaseAuth
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

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.goNavi.setOnClickListener {
            val intent = Intent(this, NaviActivity::class.java)
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
            val intent = Intent(this, NaviActivity::class.java)
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
}