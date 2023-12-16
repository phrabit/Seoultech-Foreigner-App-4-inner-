package com.example.a4_inner.post

import android.graphics.drawable.Drawable
import java.util.Date

data class Comment(
    val img: Drawable? = null,
    var comment_id: String? = null,
    val content: String? = null,
    val user_id: String? = null,
    val creationTime: java.util.Date? = null,
    val postId: String? = null
)