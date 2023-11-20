package com.example.a4_inner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a4_inner.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        binding.toLoginBtn.setOnClickListener {
            // Create an Intent to start the LoginActivity
            val intent = Intent(this, LogInActivity::class.java)

            // Optional: Add any extra information to the intent
            // intent.putExtra("key", "value")

            // Start the LoginActivity
            startActivity(intent)

            // Optional: Finish the current activity to remove it from the back stack
            finish()
        }


        setContentView(binding.root)
    }

    //TODO check if it is first time opening the app or not
    fun onBoardingVerify() {
        // if it is first time, onboarding page show

        // if it is not, directly navigate to login page
    }
}