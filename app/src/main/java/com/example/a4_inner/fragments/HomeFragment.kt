package com.example.a4_inner.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.a4_inner.CurrentUser
import com.example.a4_inner.FragmentTags
import com.example.a4_inner.R
import com.example.a4_inner.databinding.FragmentHomeBinding


// TODO: Rename parameter arguments, choose names that match


class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DOUBLEDOUBLE", "qweqwe")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        var today_class_fragment : TodayClassFragment? = childFragmentManager.findFragmentById(R.id.todayClassFragment) as? TodayClassFragment
        if(!(today_class_fragment is TodayClassFragment)){
            today_class_fragment = TodayClassFragment()
            childFragmentManager.beginTransaction()
                .add(R.id.todayClassFragment, today_class_fragment, FragmentTags.TAG_TODAY_CLASS)
                .commit()
        }
        var recent_dest_fragment : RecentDestinationFragment? = childFragmentManager.findFragmentById(R.id.recentDestinationFragment) as? RecentDestinationFragment
        if(!(recent_dest_fragment is RecentDestinationFragment)){
            recent_dest_fragment = RecentDestinationFragment()
            childFragmentManager.beginTransaction()
                .add(R.id.recentDestinationFragment, recent_dest_fragment, FragmentTags.TAG_RECENT_DEST)
                .commit()
        }
        var recent_post_fragment : RecentPostFragment? = childFragmentManager.findFragmentById(R.id.recentPostFragment) as? RecentPostFragment
        if(!(recent_post_fragment is RecentPostFragment )){
            recent_post_fragment = RecentPostFragment()
            childFragmentManager.beginTransaction()
                .add(R.id.recentPostFragment, recent_post_fragment, FragmentTags.TAG_RECENT_POST)
                .commit()
        }
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    fun refresh() {
        binding.nameTxt.text = CurrentUser.getName + "(" + CurrentUser.nation + ")"
        binding.studentNumberTxt.text = CurrentUser.stuNum
        binding.departmentTxt.text = CurrentUser.department
        binding.gradeTxt.text = CurrentUser.grade.split(";")[1]
        val recent_dest_fragment = childFragmentManager.findFragmentById(R.id.recentDestinationFragment)
        if(recent_dest_fragment is RecentDestinationFragment){
            recent_dest_fragment.redraw()
        }

        val today_class_fragment = childFragmentManager.findFragmentById(R.id.todayClassFragment)
        if(today_class_fragment is TodayClassFragment){
            today_class_fragment.getTodayClassData()
        }

        val recent_post_fragment = childFragmentManager.findFragmentById(R.id.recentPostFragment)
        if(recent_post_fragment is RecentPostFragment){
            recent_post_fragment.refresh()
        }
    }
    override fun onResume() {
        super.onResume()
        // Load the user's photo into the ImageView using Glide
        val photoUrl: Uri? = CurrentUser.getPhotoUrl
        // Reference to the ImageView
        val userPhotoImageView: ImageView = binding.userPhotoImageView
        Glide.with(this)
            .load(CurrentUser.getPhotoUrl)
            .placeholder(R.drawable.user) // Placeholder image while loading
            .error(R.drawable.user) // Error image if loading fails
            .circleCrop()
            .into(userPhotoImageView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userNameText: TextView = binding.nameTxt

        userNameText.text = CurrentUser.getName
    }

    // destroy view for Fragment
    override fun onDestroyView() {
        super.onDestroyView()
    }
}