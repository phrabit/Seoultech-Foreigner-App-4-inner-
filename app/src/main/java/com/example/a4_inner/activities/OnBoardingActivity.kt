package com.example.a4_inner.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.a4_inner.OnBoardingFragments.FifthFragment
import com.example.a4_inner.OnBoardingFragments.FirstFragment
import com.example.a4_inner.OnBoardingFragments.FourthFragment
import com.example.a4_inner.OnBoardingFragments.FragmentAdapter
import com.example.a4_inner.OnBoardingFragments.SecondFragment
import com.example.a4_inner.OnBoardingFragments.ThirdFragment
import com.example.a4_inner.PreferenceHelper
import com.example.a4_inner.databinding.ActivityOnBoardingBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create Fragments for each onboarding page
        val fragList = listOf(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment(),
            FourthFragment(),
            FifthFragment()
        )

        // Set up ViewPager2 with FragmentAdapter
        val fragAdapter = FragmentAdapter(this)
        fragAdapter.fragList = fragList
        binding.viewPager.adapter = fragAdapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "${position + 1}"
        }.attach()

        // Set up button click listener
        binding.btnSkip.setOnClickListener {
            binding.viewPager.currentItem = fragAdapter.itemCount - 1
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

        // Set up page change callback
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    in 0..3 -> {
                        binding.btnSkip.visibility = View.VISIBLE
                        binding.toLoginBtn.visibility = View.GONE
                    }
                    4 -> {
                        binding.btnSkip.visibility = View.GONE
                        binding.toLoginBtn.visibility = View.VISIBLE
                    }
                }
            }
        })


        // if onBoarding Toggle is OFF(false), directly go to LogInActivity.
        if(!PreferenceHelper.getBoolean(this)) {
            // Create an Intent to start the LoginActivity
            val intent = Intent(this, LogInActivity::class.java)

            // Start the LoginActivity
            startActivity(intent)

            finish()
        }


    }
}