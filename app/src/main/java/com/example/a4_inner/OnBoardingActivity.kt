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

        // if onBoarding Toggle is OFF(false), directly go to LogInActivity.
        if(!PreferenceHelper.getBoolean(this)) {
            // Create an Intent to start the LoginActivity
            val intent = Intent(this, LogInActivity::class.java)

            // Start the LoginActivity
            startActivity(intent)

            finish()
        }

        binding.toLoginBtn.setOnClickListener {
            // If onBoarding successfully ended, onBoardingToggle OFF.
            PreferenceHelper.setBoolean(this, false)

            // Create an Intent to start the LoginActivity
            val intent = Intent(this, LogInActivity::class.java)

            // Start the LoginActivity
            startActivity(intent)

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