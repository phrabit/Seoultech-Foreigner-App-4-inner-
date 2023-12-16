// 추후 간추리기 용!!!!

package com.example.a4_inner.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.a4_inner.databinding.ActivityMyPageBinding
import androidx.activity.result.ActivityResultLauncher


class MyPageActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMyPageBinding
    private val callback = createOnBackPressedCallback()
    private val pickImageResultLauncher = registerForImageSelection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupOnBackPressed()
        setupImageSelection()
        setupOnBoardingToggle()
        setupLogoutButton()
    }

    // Setup methods
    private fun setupUI() {
        // Your UI setup logic here
    }

    private fun setupOnBackPressed() {
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setupImageSelection() {
        // Your image selection setup logic here
    }

    private fun setupOnBoardingToggle() {
        // Your onboarding toggle setup logic here
    }

    private fun setupLogoutButton() {
        // Your logout button setup logic here
    }

    // OnBackPressed related methods
    private fun createOnBackPressedCallback(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        }
    }

    private fun handleBackPressed() {
        if (binding.editBtn.text == "Complete") {
            showUnsavedChangesDialog()
        } else {
            navigateToNaviActivity()
        }
    }

    private fun showUnsavedChangesDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("")
            setMessage("You have unsaved changes. Are you sure you want to leave?")
            setPositiveButton("Yes") { _, _ ->
                navigateToNaviActivity()
            }
            setNegativeButton("No", null)
            show()
        }
    }

    private fun navigateToNaviActivity() {
        Intent(this, NaviActivity::class.java).also { intent ->
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    // Image selection and upload methods
    private fun registerForImageSelection(): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            handleImageSelection(uri)
        }
    }

    private fun handleImageSelection(uri: Uri?) {
        uri?.let {
            uploadImageToStorage(it)
        }
    }

    private fun uploadImageToStorage(uri: Uri) {
        // Your Firebase storage upload logic here
    }

    private fun updatePhotoUrlInFirestore(photoUrl: String) {
        // Your Firestore update logic here
    }

    private fun loadImageWithGlide(photoUrl: String) {
        // Your Glide loading logic here
    }
}