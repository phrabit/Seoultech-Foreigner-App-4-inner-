package com.example.a4_inner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a4_inner.databinding.FragmentBulletinBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import java.security.Timestamp
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BulletinFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BulletinFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentBulletinBinding
    private lateinit var adapter: RecyclerUserAdapter
    private var list = ArrayList<Board>()
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBulletinBinding.inflate(inflater, container, false)

        // Firestore 초기화
        firestore = FirebaseFirestore.getInstance()

        // Firestore에서 데이터 가져와 RecyclerView 업데이트
        fetchDataFromFirestore()

        // 남섭이형을 위해 최근게시물 3개 불러오는 함수 호출.
        fetchRecentBulletinData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "1", "name 1"))

        val recyclerView: RecyclerView = binding.lstUser

        adapter = RecyclerUserAdapter(list)

        adapter.setOnItemClickListener(object : RecyclerUserAdapter.OnItemClickListener {
            override fun onItemClick(data: Board, pos: Int) {
                val intent = Intent(requireContext(), Posting::class.java)
                intent.putExtra("Title", data.title)
                intent.putExtra("Contents", data.contents)
                intent.putExtra("PostId", data.documentId)  // Document Id를 Intent에 추가
                startActivity(intent)
            }
        })

        binding.postBtn.setOnClickListener {
            val intent = Intent(requireContext(), Post::class.java)
            startActivityForResult(intent, POST_ACTIVITY_REQUEST_CODE)
        }

        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val boardData = data?.getSerializableExtra("boardData") as? Board

            if (boardData != null) {
                // Check if the boardData already exists in the list
                val existingBoard = list.find { it.title == boardData.title && it.contents == boardData.contents }

                if (existingBoard != null) {
                    // Update the existing board in the list
                    existingBoard.title = boardData.title
                    existingBoard.contents = boardData.contents
                    adapter.notifyDataSetChanged()
                } else {
                    // Add the new boardData to the list
                    addNewItem(boardData)
                }
            }
        } else {
            Log.d("ITM", "Invalid requestCode or resultCode: requestCode=$requestCode, resultCode=$resultCode")
        }
    }

    // Firestore에서 데이터를 실시간으로 가져오는 함수
    fun fetchDataFromFirestore() {
        MainScope().launch {
            firestore.collection("Board")
                .orderBy("creationTime", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Log.e("ITM", "Error getting documents: ", exception)
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val newDataList = ArrayList<Board>()

                        for (document in snapshot) {
                            // Firestore 문서를 데이터 모델로 변환 (이 경우 Board)
                            val title = document.getString("title")
                            val contents = document.getString("contents")
                            val user_id = document.getString("user_id")

                            if (!title.isNullOrBlank() && !contents.isNullOrBlank() && !user_id.isNullOrBlank()) {
                                newDataList.add(
                                    Board(
                                        img = R.drawable.user,
                                        title = title!!,
                                        contents = contents!!,
                                        user_id = user_id!!,
                                        documentId = document.id // 문서 ID를 저장합니다.
                                    )
                                )
                            }
                        }

                        // RecyclerView 데이터 업데이트
                        list.clear()
                        list.addAll(newDataList)
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.d("ITM", "Current data: null")
                    }
                }
        }
    }



    ////////////////////////////////////////////////////////////////

    // 남섭이형을 위한 코드
    // RecentBulletinData 객체를 사용하면 최근 3개 게시물 리스트 형태로 반환받을 수 있음.

    object RecentBulletinData {
        private var recentBulletinList: List<Board>? = null

        fun setRecentBulletinList(list: List<Board>) {
            recentBulletinList = list
        }

        fun getRecentBulletinList(): List<Board>? {
            return recentBulletinList
        }
    }

    fun fetchRecentBulletinData() {
        MainScope().launch {
            firestore.collection("Board")
                .orderBy("creationTime", Query.Direction.DESCENDING)
                .limit(3) // 최근 3개의 문서만 가져오도록 설정
                .get()
                .addOnSuccessListener { documents ->
                    val recentBulletinList = ArrayList<Board>()

                    for (document in documents) {
                        // Firestore 문서를 데이터 모델로 변환 (이 경우 Board)
                        val title = document.getString("title")
                        val contents = document.getString("contents")
                        val user_id = document.getString("user_id")

                        if (!title.isNullOrBlank() && !contents.isNullOrBlank() && !user_id.isNullOrBlank()) {
                            recentBulletinList.add(
                                Board(
                                    img = R.drawable.user,
                                    title = title!!,
                                    contents = contents!!,
                                    user_id = user_id!!,
                                    documentId = document.id // 문서 ID를 저장합니다.
                                )
                            )
                        }
                    }

                    // 최근 3개의 게시물 데이터를 싱글톤에 저장
                    RecentBulletinData.setRecentBulletinList(recentBulletinList)
                }
                .addOnFailureListener { exception ->
                    Log.e("ITM", "Error getting recent documents: ", exception)
                }
        }
    }



    ////////////////////////////////////////////////////////////////
    fun addNewItem(item: Board) {
        // RecyclerView의 맨 위에 아이템 추가
        list.add(0, item)
        adapter.notifyItemInserted(0)
    }

    // public 메서드를 통해 adapter 반환
    fun getAdapter(): RecyclerUserAdapter {
        return adapter
    }

    companion object {
        const val POST_ACTIVITY_REQUEST_CODE = 1

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BulletinFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BulletinFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}