package com.example.a4_inner

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.a4_inner.databinding.ActivityPostBinding
import com.example.a4_inner.databinding.ActivityUpdatePostBinding
import com.google.firebase.firestore.FirebaseFirestore

class updatePost : AppCompatActivity() {

    lateinit var binding: ActivityUpdatePostBinding
    private lateinit var firestore: FirebaseFirestore
    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로 전달된 데이터 받기
        val title = intent.getStringExtra("Title")
        val contents = intent.getStringExtra("Contents")
        documentId = intent.getStringExtra("PostId")  // Intent에서 PostId를 받아옵니다.

        // 받아온 데이터를 EditText에 설정
        binding.titleInsert.setText(title)
        binding.contentsInsert.setText(contents)

        // Firestore 초기화
        firestore = FirebaseFirestore.getInstance()

        // Complete 버튼 클릭 이벤트 처리
        binding.completeBtn.setOnClickListener {
            // 새로운 title과 contents를 가져옴
            val newTitle = binding.titleInsert.text.toString()
            val newContents = binding.contentsInsert.text.toString()

            // 확인을 위한 로그 추가
            Log.d("ITM", "Complete button clicked. NewTitle: $newTitle, NewContents: $newContents")

            // Intent를 생성하여 새로운 title과 contents를 activity_posting에 전달
            val resultIntent = Intent().apply {
                putExtra("NewTitle", newTitle)
                putExtra("NewContents", newContents)
            }
            setResult(Activity.RESULT_OK, resultIntent)

            // Firestore DB 업데이트
            updateFirestoreData(newTitle, newContents)
            Log.d("ITM","$newTitle, $newContents")


            // 현재 액티비티 종료
            finish()
        }
    }

    private fun updateFirestoreData(newTitle: String, newContents: String) {
        documentId?.let { docId ->
            Log.d("ITM", "Updating Firestore document. DocumentId: $docId, NewTitle: $newTitle, NewContents: $newContents")
            val documentReference = firestore.collection("Board").document(docId)

            // 업데이트할 데이터 생성
            val updatedData = hashMapOf(
                "title" to newTitle,
                "contents" to newContents
            )

            Log.d("ITM", "Updated data: $updatedData")

            // Firestore 문서 업데이트
            documentReference.update(updatedData as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d("ITM", "Firestore document updated successfully.")
                }
                .addOnFailureListener { exception ->
                    Log.d("ITM", "Error updating Firestore document:$exception" )
                }
        }
    }
}