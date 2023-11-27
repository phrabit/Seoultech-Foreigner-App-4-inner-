package com.example.a4_inner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerUserAdapter(private val items: ArrayList<Board>) : RecyclerView.Adapter<RecyclerUserAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: Board, pos:Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // 아이템 개수 가져오기
    override fun getItemCount(): Int = items.size

    // 항목 뷰에 데이터 연결하기
    override fun onBindViewHolder(holder: RecyclerUserAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.apply {
            bind(item)
            itemView.tag = item
            itemView.setOnClickListener {
                listener?.onItemClick(item, position)
            }
        }
    }

    // 항목에 사용할 View 생성하고 ViewHolder 반환하기
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return RecyclerUserAdapter.ViewHolder(inflatedView)
    }

    // 각 항목에 필요한 기능을 구현
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(item: Board) {
            // 수정된 부분: 뷰의 형식에 맞게 캐스팅
            val userItemTitleTextView: TextView = view.findViewById(R.id.item_title)
            val userItemContentsTextView: TextView = view.findViewById(R.id.comment_li)

            userItemTitleTextView.text = item.title
            userItemContentsTextView.text = item.contents
        }
    }
}