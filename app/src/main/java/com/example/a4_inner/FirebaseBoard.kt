package com.example.a4_inner

import java.util.Date


// 게시판 Firestore DB 연습
data class FirebaseBoard(
    var title: String,
    val contents: String,
    val creationTime: Date,
    val documentId: String? = null  // Document ID를 저장할 필드 추가
)