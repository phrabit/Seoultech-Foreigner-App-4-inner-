package com.example.a4_inner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RecyclerCommentAdapter(private val commentList: ArrayList<Comment>) : RecyclerView.Adapter<RecyclerCommentAdapter.ViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comments_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = commentList[position]
        holder.bind(comment)

        holder.itemView.findViewById<ImageView>(R.id.delete).setOnClickListener {
            deleteComment(position)
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment: Comment) {
            val userIconImageView: ImageView = itemView.findViewById(R.id.imgUser_icon)
            val commentTextView: TextView = itemView.findViewById(R.id.comments)

            userIconImageView.setImageDrawable(comment.img)
            commentTextView.text = comment.content
        }
    }

    private fun deleteComment(position: Int) {
        val commentToDelete = commentList[position]

        firestore.collection("Comment").document(commentToDelete.comment_id!!)
            .delete()
            .addOnSuccessListener {
                commentList.removeAt(position)
                notifyItemRemoved(position)
            }
            .addOnFailureListener { e ->
                // 로그를 출력하거나 사용자에게 에러 메시지를 표시합니다.
            }
    }

}