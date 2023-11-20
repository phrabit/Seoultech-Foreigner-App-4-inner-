package com.example.a4_inner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerCommentAdapter(private val commentList: ArrayList<UserData>) : RecyclerView.Adapter<RecyclerCommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comments_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = commentList[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment: UserData) {
            val userIconImageView: ImageView = itemView.findViewById(R.id.imgUser_icon)
            val commentTextView: TextView = itemView.findViewById(R.id.comments)

            userIconImageView.setImageDrawable(comment.img)
            commentTextView.text = comment.contents
        }
    }
}
