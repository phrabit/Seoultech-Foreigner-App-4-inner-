package com.example.a4_inner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.a4_inner.databinding.ActivityPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Date

class Post : AppCompatActivity() {

    lateinit var binding: ActivityPostBinding
//    lateinit var recyclerView: RecyclerView
//    lateinit var adapter: RecyclerUserAdapter
//    private val firestoreCollection = "FirebaseBoard_practice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val originalTitle = intent.getStringExtra("Title")
        val originalContents = intent.getStringExtra("Contents")

        // 받아온 데이터를 EditText에 설정
        binding.titleInsert.setText(originalTitle)
        binding.contentsInsert.setText(originalContents)

        binding.completeBtn.setOnClickListener {
            val title = binding.titleInsert.text.toString()
            val contents = binding.contentsInsert.text.toString()

            // 데이터가 비어있는지 확인
            if (title.isNotEmpty() && contents.isNotEmpty()) {
                // 현재 시간 가져오기
                val currentTime = Calendar.getInstance().time
                val user_id = FirebaseAuth.getInstance().currentUser?.uid ?: "dummy" // 로그인한 사용자의 고유 ID를 가져옵니다.

                val boardRef = FirebaseFirestore.getInstance().collection("Board").document()
                val documentId = boardRef.id  // 임시 documentId를 생성합니다.

                val board = Board(
                    img = R.drawable.user,
                    title = title,
                    contents = contents,
                    creationTime = currentTime,
                    user_id = user_id,
                    comments = listOf(),
                    documentId = documentId
                )

                // 생성한 documentId를 사용하여 게시글을 추가합니다.
                boardRef.set(board)
                    .addOnSuccessListener {
                        Log.d("ITM", "Document added with ID: $documentId")

                        // setResult 호출로 데이터 전달
                        val resultIntent = Intent()
                        resultIntent.putExtra("Board", board)
                        setResult(Activity.RESULT_OK, resultIntent)
                        Log.d("ITM","${Activity.RESULT_OK} - 이거 맞나?")
                        finish()  // Post 액티비티 종료

                    }
                    .addOnFailureListener { exception ->
                        Log.d("ITM", "Error adding document", exception)
                    }



            } else {
                // 데이터가 비어있을 경우 사용자에게 알림 등을 표시할 수 있음
                Toast.makeText(this@Post, "Please enter both title and contents", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun setDocument(data: Board) {
//        Log.d("ITM", "setDocument called")
//        FirebaseFirestore.getInstance()
//            .collection("Board")
//            .add(data)
//            .addOnSuccessListener { documentReference ->
//                Log.d("ITM", "Document added successfully. Document ID: ${documentReference.id}")
//                // setResult 호출로 데이터 전달
//                val resultIntent = Intent()
//                resultIntent.putExtra("Board", data)
//                setResult(Activity.RESULT_OK, resultIntent)
//                Log.d("ITM","${Activity.RESULT_OK} - 이거 맞나?")
//                finish()
//            }
//            .addOnFailureListener {
//                Log.d("ITM", "Document added Fail - What the Fuck")
//            }
//    }
}