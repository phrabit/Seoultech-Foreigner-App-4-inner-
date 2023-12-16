package com.example.a4_inner

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.a4_inner.databinding.ActivityMyPageBinding
import com.example.a4_inner.databinding.ActivityNaviBinding
import com.example.a4_inner.databinding.FragmentHomeBinding
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore


class MyPageActivity : AppCompatActivity() {

    // 뒤로가기 재정의
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // '수정하기' 모드인지 확인
            if(binding.editBtn.text == "Complete") {
                // 대화상자를 표시하여 사용자에게 경고
                AlertDialog.Builder(this@MyPageActivity).apply {
                    setTitle("")
                    setMessage("You have unsaved changes. Are you sure you want to leave?")
                    setPositiveButton("Yes") { _, _ ->
                        // If the user chooses 'Yes', navigate to NaviActivity
                        navigateToNaviActivity()
                    }
                    setNegativeButton("No", null)
                    show()
                }
            } else {
                navigateToNaviActivity()
            }
        }
    }
    private fun navigateToNaviActivity() {
        // NaviActivity로 이동하도록 Intent를 생성합니다.
        val intent = Intent(this@MyPageActivity, NaviActivity::class.java)
        // 이전의 액티비티 스택을 모두 지우고 새로운 액티비티를 시작합니다.
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private val pickImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                // Firebase Storage에 이미지 업로드
                val storageReference =
                    FirebaseStorage.getInstance().reference.child("UserImages/${CurrentUser.getUserUid}")
                storageReference.putFile(uri)
                    .addOnSuccessListener { taskSnapshot ->
                        // 이미지 업로드 성공, 업로드된 이미지의 URL 받기
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                            // Firestore에 이미지 URL 저장
                            val userRef = FirestoreProvider.db.collection("users")
                                .document(CurrentUser.getUserUid!!)
                            userRef.update("photoUrl", downloadUri.toString())
                                .addOnSuccessListener {
                                    Log.d("ITM", "Photo URL successfully updated.")
                                    // 이미지가 성공적으로 업데이트되면 Glide로 이미지 다시 로드
                                    Glide.with(this@MyPageActivity)
                                        .load(downloadUri)
                                        .placeholder(R.drawable.user) // Placeholder image while loading
                                        .error(R.drawable.user) // Error image if loading fails
                                        .circleCrop()
                                        .into(binding.userPhotoImageView2)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("ITM", "Error updating photo URL.", e)
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("ITM", "Error uploading image.", e)
                    }
            }
        }
    private lateinit var binding: ActivityMyPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // grade variable
        val grades = arrayOf("1st grade; freshman", "2nd grade; sophomore", "3rd grade; junior", "4th grade; senior")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades)
        binding.gradeSpinner.visibility = View.GONE
        binding.depEditText.visibility = View.GONE
        binding.nationEditText.visibility = View.GONE
        binding.stuNumEditText.visibility = View.GONE
        setTextView(binding.depText, CurrentUser.department)
        setTextView(binding.stuNumText, CurrentUser.stuNum)
        setTextView(binding.gradeText, CurrentUser.grade)
        setTextView(binding.nationText, CurrentUser.nation)

        // 뒤로가기 재정의
        this.onBackPressedDispatcher.addCallback(this, callback)

        //  Assuming you have the user's photo URI stored in a variable named photoUri
        val photoUrl: Uri? = CurrentUser.getPhotoUrl

        // Reference to the ImageView
        val userPhotoImageView: ImageView = binding.userPhotoImageView2
        val userNameText: TextView = binding.nameText

        userNameText.text = CurrentUser.getName

        // Load the user's photo into the ImageView using Glide
        Glide.with(this)
            .load(photoUrl)
            .placeholder(R.drawable.user) // Placeholder image while loading
            .error(R.drawable.user) // Error image if loading fails
            .circleCrop()
            .into(userPhotoImageView)

        userPhotoImageView.setOnClickListener {
            // 이미지 뷰를 클릭하면 이미지 선택 인텐트를 시작
            pickImageResultLauncher.launch("image/*")
        }

        binding.gradeSpinner.adapter = adapter

        // EDIT ON/OFF
        binding.editBtn.setOnClickListener {
            val isInEditMode = binding.editBtn.text == "Edit Profile"
            toggleEditMode(isInEditMode)
            binding.depEditText.setText(CurrentUser.department)
            binding.stuNumEditText.setText(CurrentUser.stuNum)
            binding.nationEditText.setText(CurrentUser.nation)

            val gradeIndex = grades.indexOf(CurrentUser.grade)
            if (gradeIndex != -1) {
                binding.gradeSpinner.setSelection(gradeIndex)
            }
        }

        if (PreferenceHelper.getBoolean(this)) {
            binding.onBoardingText.text = "onBoarding: ON"
        }

        // Get pref and set the initial value
        val initialSwitchState = PreferenceHelper.getBoolean(this)
        binding.onBoardingToggle.isChecked = initialSwitchState

        // Update the pref as toggle changes
        binding.onBoardingToggle.setOnCheckedChangeListener { _, isChecked ->
            PreferenceHelper.setBoolean(this, isChecked)
            if (isChecked) {
                // If toggle true -> onBoarding ON
                binding.onBoardingText.text = "onBoarding: ON"
            } else {
                // If toggle false -> onBoarding OFF
                binding.onBoardingText.text = "onBoarding: OFF"
            }
        }

        binding.logOutButton2.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Log Out")
            builder.setMessage("Are you sure?")

            builder.setPositiveButton("Yes") { _, _ ->
                // sign out
                try {
                    Firebase.auth.signOut()
                    CurrentUser.logout()
                    Log.d("ITM", "successfully signed out")
                } catch (e: ApiException) {
                    Log.d("ITM", "Sign out failed")
                }
                val intent = Intent(this, LogInActivity::class.java);
                startActivity(intent)
            }

            builder.setNegativeButton("No") { _, _ ->
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    // Edit mode functions.
    private fun toggleEditMode(isInEditMode: Boolean) {
        if (!isInEditMode) {
            // 모든 입력 필드가 비어있는지 확인
            if (binding.depEditText.text.toString().isEmpty() ||
                binding.nationEditText.text.toString().isEmpty() ||
                binding.stuNumEditText.text.toString().isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return
            }

            uploadAdditionalInfo()

            binding.gradeText.text = binding.gradeSpinner.selectedItem.toString()
            binding.depText.text = binding.depEditText.text.toString()
            binding.nationText.text = binding.nationEditText.text.toString()
            binding.stuNumText.text = binding.stuNumEditText.text.toString()

            // 입력이 완료되면 텍스트 색상을 검은색으로 변경
            binding.gradeText.setTextColor(Color.BLACK)
            binding.depText.setTextColor(Color.BLACK)
            binding.nationText.setTextColor(Color.BLACK)
            binding.stuNumText.setTextColor(Color.BLACK)
        }

        val visibilityInEditMode = if (isInEditMode) View.VISIBLE else View.GONE
        val visibilityInViewMode = if (isInEditMode) View.GONE else View.VISIBLE

        binding.gradeSpinner.visibility = visibilityInEditMode
        binding.depEditText.visibility = visibilityInEditMode
        binding.nationEditText.visibility = visibilityInEditMode
        binding.stuNumEditText.visibility = visibilityInEditMode

        binding.gradeText.visibility = visibilityInViewMode
        binding.depText.visibility = visibilityInViewMode
        binding.nationText.visibility = visibilityInViewMode
        binding.stuNumText.visibility = visibilityInViewMode

        binding.editBtn.text = if (isInEditMode) "Complete" else "Edit Profile"
    }

    private fun uploadAdditionalInfo() {
        // Firebase Firestore에 데이터를 업로드하는 부분
        val db = Firebase.firestore
        val user = hashMapOf(
            "department" to binding.depEditText.text.toString(),
            "stuNumber" to binding.stuNumEditText.text.toString(),
            "grade" to binding.gradeSpinner.selectedItem.toString(),
            "nation" to binding.nationEditText.text.toString()
        )
        // Firebase에 접속하여 정보 입력하는 부분!!
        val docRef = db.collection("users").document(CurrentUser.getUserUid!!)
        docRef.set(user, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    private fun setTextView(textView: TextView, value: String) {
        textView.text = value
        if (value == "NOT SET YET") {
            textView.setTextColor(ContextCompat.getColor(this, R.color.st_red ))
        } else {
            textView.setTextColor(Color.BLACK)
        }
    }

}