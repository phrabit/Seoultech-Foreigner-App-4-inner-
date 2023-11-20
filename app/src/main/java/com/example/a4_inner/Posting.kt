package com.example.a4_inner

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a4_inner.databinding.ActivityPostingBinding

class Posting : AppCompatActivity() {

    lateinit var binding: ActivityPostingBinding
    private lateinit var commentAdapter: RecyclerCommentAdapter
    private val commentList: ArrayList<UserData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 어댑터 초기화
        commentAdapter = RecyclerCommentAdapter(commentList)
        binding.commentLi.adapter = commentAdapter

        // RecyclerView 레이아웃 매니저 설정
        binding.commentLi.layoutManager = LinearLayoutManager(this)


        /////////////////////////////////////////////////////////////////

        // Intent로 전달된 데이터 받기
        val title = intent.getStringExtra("Title")
        val contents = intent.getStringExtra("Contents")

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



        // commentsBtn 클릭 이벤트 처리
        binding.commentsBtn.setOnClickListener {
            Log.d("ITM", "Button clicked - Before addComment()")
            addComment()
            Log.d("ITM", "Button clicked - After addComment()")
        }
    }



    private fun addComment() {
        Log.d("ITM", "Button clicked") // 버튼이 클릭되었는지 확인하는 로그 추가

        val commentText = binding.comments.text.toString().trim()

        if (TextUtils.isEmpty(commentText)) {
            Toast.makeText(this, "Please insert the comments.", Toast.LENGTH_SHORT).show()
            return
        }

        // 이미지 리소스 가져오기
        val drawableId: Int = R.drawable.user
        val imageDrawable: Drawable? = ContextCompat.getDrawable(this, drawableId)

        // 댓글 목록에 추가
        val newComment = UserData(img = imageDrawable!!, title = "User", contents = commentText)
        commentList.add(newComment)

        // RecyclerView 갱신
        commentAdapter.notifyDataSetChanged()

        // 댓글 입력창 초기화
        binding.comments.text.clear()

        // Log 추가
        Log.d("ITM", "Comment added: $commentText")
    }

}