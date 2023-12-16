package com.example.a4_inner

import android.util.Log
import com.example.a4_inner.fragments.HomeFragment
import com.google.firebase.firestore.Query
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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

fun fetchRecentBulletinData(home_fragment : HomeFragment?) {
    MainScope().launch {
        FireBase.db.collection("Board")
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
                home_fragment?.refresh()
            }
            .addOnFailureListener { exception ->
                Log.e("ITM", "Error getting recent documents: ", exception)
            }
    }
}



////////////////////////////////////////////////////////////////