package com.example.a4_inner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a4_inner.databinding.ActivityPostBinding

class Post : AppCompatActivity() {

    lateinit var binding: ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.completeBtn.setOnClickListener {
            val title = binding.titleInsert.text.toString()
            val contents = binding.contentsInsert.text.toString()

            // 데이터가 비어있는지 확인
            if (title.isNotEmpty() && contents.isNotEmpty()) {
                // MainActivity로 돌아가기 위한 Intent 생성
                val intent = Intent()

                // 데이터를 Intent에 추가
                intent.putExtra("Title", title)
                intent.putExtra("Contents", contents)

                // 결과를 MainActivity로 전달하고 현재 액티비티 종료
                setResult(Activity.RESULT_OK, intent)
                finish()

                // 로그를 추가하여 확인
                Log.d("ITM", "Data sent successfully: Title=$title, Contents=$contents")
            } else {
                // 데이터가 비어있을 경우 사용자에게 알림 등을 표시할 수 있음
                Toast.makeText(this@Post, "Please enter both title and contents", Toast.LENGTH_SHORT).show()
            }
        }
    }
}