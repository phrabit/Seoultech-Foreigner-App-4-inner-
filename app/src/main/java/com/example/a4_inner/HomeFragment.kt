package com.example.a4_inner

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.a4_inner.databinding.FragmentHomeBinding
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


// TODO: Rename parameter arguments, choose names that match


class HomeFragment : Fragment() {

    // binding for fragment
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // Load the user's photo into the ImageView using Glide
    private var photoUrl: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }
    fun refresh() {
        val recent_dest_fragment = childFragmentManager.findFragmentById(R.id.recentDestinationFragment)
        if(recent_dest_fragment is RecentDestinationFragment){
            recent_dest_fragment.redraw()
        }
    }
    override fun onResume() {
        super.onResume()
        Log.d("pref", "ASdadasdadd")
        // Load the user's photo into the ImageView using Glide
        photoUrl = CurrentUser.getPhotoUrl
        // Reference to the ImageView
        val userPhotoImageView: ImageView = binding.userPhotoImageView

        Glide.with(this)
            .load(photoUrl)
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
        _binding = null
    }
}