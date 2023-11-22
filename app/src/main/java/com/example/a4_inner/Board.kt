package com.example.a4_inner

import android.graphics.drawable.Drawable
// *목록에 보여줄 값 데이터 클래스 생성*

// 목록엔 제목 컨텐츠을 보여줄것
data class Board(
    var img: Drawable?,
    var title: String,
    val contents: String,
)