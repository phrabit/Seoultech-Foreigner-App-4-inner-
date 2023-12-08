package com.example.a4_inner

import android.graphics.drawable.Drawable
import com.google.firebase.firestore.DocumentReference
import java.io.Serializable
import java.util.Date

// *목록에 보여줄 값 데이터 클래스 생성*

// 목록엔 제목 컨텐츠을 보여줄것
data class Board(
    var img: Int,
    var contents: String = "",
    val creationTime: Date = Date(),
    var title: String = "",
    val user_id: String = "",
    val comments: List<String> = emptyList(),
    var documentId: String = "" // delete와 update를 위함.
):Serializable

