package com.example.a4_inner

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a4_inner.databinding.ActivityPostingBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import java.security.Timestamp
import java.util.Calendar

class Posting : AppCompatActivity() {

    lateinit var binding: ActivityPostingBinding
    private lateinit var commentAdapter: RecyclerCommentAdapter
    private val commentList: ArrayList<Comment> = ArrayList()

    private lateinit var commentListener: ListenerRegistration

    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 어댑터 초기화FE
        commentAdapter = RecyclerCommentAdapter(commentList)
        binding.commentLi.adapter = commentAdapter

        // RecyclerView 레이아웃 매니저 설정
        binding.commentLi.layoutManager = LinearLayoutManager(this)


        /////////////////////////////////////////////////////////////////

        // Intent로 전달된 데이터 받기
        val title = intent.getStringExtra("Title")
        val contents = intent.getStringExtra("Contents")
        val postId = intent.getStringExtra("PostId")  // 문서 ID를 받아옵니다.
        watchComments()
        // 받아온 데이터를 TextView에 설정
        binding.itemTitle.text = title

        // contentsView에 contents 값 설정
        if (contents != null) {
            binding.contentsView.text = contents
        } else {
            // contents가 null일 경우 대비한 처리
            binding.contentsView.text = "No contents available"
        }

        // Contents 값 로그 출력
        Log.d("ITM", "Contents from Intent: $contents")

        /////////////////////////////////////////////////////////////////

        // commentsBtn 클릭 이벤트 처리
        binding.commentsBtn.setOnClickListener {
            Log.d("ITM", "Button clicked - Before addComment()")
            try {
                addComment()
            } catch (e: Exception) {
                Log.e("ITM", "Exception in addComment()", e)
            }
            Log.d("ITM", "Button clicked - After addComment()")
        }


        /////////////////////////////////////////////////////////////////

        // Spinner 초기화
        val spinnerItems = arrayOf("Update", "Delete")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems)

        binding.imageButton.setOnClickListener {
            showOptionsDialog(adapter, postId!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 액티비티가 종료될 때 Firebase Firestore 리스너 제거
        if (this::commentListener.isInitialized) {
            commentListener.remove()
        }
    }



    private fun addComment() {
        val commentText = binding.comments.text.toString().trim()

        if (TextUtils.isEmpty(commentText)) {
            Toast.makeText(this, "Please insert the comments.", Toast.LENGTH_SHORT).show()
            return
        }

        val postId = intent.getStringExtra("PostId")
        if (postId == null) {
            Log.e("ITM", "No PostId passed in intent")
            return
        }

        // 댓글 ID 생성
        val newCommentId = FireBase.db.collection("Comment").document().id

        // 댓글 목록에 추가
        val newComment = Comment(
            img = null,
            comment_id = newCommentId,  // Use the generated ID
            content = commentText,
            user_id = CurrentUser.getUserUid,
            creationTime = java.util.Date(),
            postId = postId  // postId를 댓글에 추가합니다.
        )
        commentList.add(newComment)

        // Firebase Firestore에 댓글 추가
        FireBase.db.collection("Comment").document(newCommentId)
            .set(newComment)
            .addOnSuccessListener {
                Log.d("ITM", "댓글 추가 성공, document ID: $newCommentId")

                // 게시글의 comments 필드 업데이트
                postId?.let {
                    FireBase.db.collection("Board").document(it)
                        .update("comments", FieldValue.arrayUnion(newCommentId))
                }?.addOnCompleteListener {
                    watchComments()
                }

                // RecyclerView 갱신
                commentAdapter.notifyItemInserted(commentList.size - 1)

                // 댓글 입력창 초기화
                binding.comments.text.clear()

                // Log 추가
                Log.d("ITM", "Comment added: $commentText")
            }
            .addOnFailureListener { e ->
                Log.e("ITM", "댓글 추가 실패: $e")
            }
    }

    // 게시글의 comments 필드 실시간 감시
    private fun watchComments() {
        val postId = intent.getStringExtra("PostId")
        if (postId != null) {
            val listener = FireBase.db.collection("Comment")
                .whereEqualTo("postId", postId)
                .orderBy("creationTime")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("ITM", "댓글 업데이트 실패: $error")
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        commentList.clear()
                        for (doc in snapshot.documents) {
                            val comment = doc.toObject(Comment::class.java)
                            if (comment != null) {
                                commentList.add(comment)
                            }
                        }
                        commentAdapter.notifyDataSetChanged()
                    }
                }
            listener.remove()
        }
        else {
            Log.e("ITM", "No PostId passed in intent")
        }
    }


    private fun showOptionsDialog(adapter: ArrayAdapter<String>, docId: String) {
        val options = arrayOf("Update", "Delete")

        val alertDialog = AlertDialog.Builder(this)
            .setItems(options) { _, which ->
                val selectedOption = options[which]
                handleSelectedOption(selectedOption, docId)
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }

    private fun handleSelectedOption(selectedOption: String, docId: String) {
        val db = Firebase.firestore
        val docRef = db.collection("Board").document(docId)

        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val userId = document.getString("user_id")
                if (userId == CurrentUser.getUserUid) {
                    when (selectedOption) {
                        "Update" -> {
                            // Implement the logic for update
                            // For example, you can open another activity for updating the comment
                            startUpdatePostActivity()
                        }
                        "Delete" -> {
                            // Implement the logic for delete
                            // For example, you can remove the comment from the list
                            Toast.makeText(this, "Delete selected", Toast.LENGTH_SHORT).show()
                            deletePost()
                        }
                    }
                } else {
                    Toast.makeText(this, "Only the user who wrote the post has permission.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("ITM", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("ITM", "get failed with ", exception)
        }
    }


    private fun startUpdatePostActivity() {
        val updateIntent = Intent(this, updatePost::class.java)

        val postId = intent.getStringExtra("PostId")

        // 현재 title과 contents ㅇㄴ를 updatePost에 전달
        updateIntent.putExtra("Title", binding.itemTitle.text.toString())
        updateIntent.putExtra("Contents", binding.contentsView.text.toString())
        updateIntent.putExtra("PostId", postId)  // PostId를 Intent에 추가
        startActivityForResult(updateIntent, UPDATE_POST_REQUEST)
    }

    private fun deletePost() {
        val postId = intent.getStringExtra("PostId")  // Intent에서 PostId를 받아옵니다.
        if (postId != null) {
            FireBase.db.collection("Board").document(postId)
                .delete()
                .addOnSuccessListener {
                    Log.d("ITM", "Post successfully deleted!")
                    // Post가 성공적으로 삭제되면 Activity를 종료합니다.
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w("ITM", "Error deleting post", e)
                }
        } else {
            Log.d("ITM", "No PostId passed in intent")
        }
    }


    // onActivityResult에서 업데이트된 데이터 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UPDATE_POST_REQUEST && resultCode == Activity.RESULT_OK) {
            // 업데이트된 title과 contents를 받아옴
            val newTitle = data?.getStringExtra("NewTitle")
            val newContents = data?.getStringExtra("NewContents")

            // 받아온 title과 contents를 TextView에 설정
            binding.itemTitle.text = newTitle
            binding.contentsView.text = newContents
        }
    }

    companion object {
        private const val UPDATE_POST_REQUEST = 123
    }

}