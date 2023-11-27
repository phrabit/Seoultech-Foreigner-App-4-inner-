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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "1", "name 1"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "2", "name 2"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "3", "name 3"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "4", "name 4"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "5", "name 5"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "6", "name 6"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "7", "name 7"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "8", "name 8"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "9", "name 9"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "10", "name 10"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "11", "name 11"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "12", "name 12"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "13", "name 13"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "14", "name 14"))
        list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, "15", "name 15"))

        val recyclerView: RecyclerView = binding.lstUser

        adapter = RecyclerUserAdapter(list)

        adapter.setOnItemClickListener(object : RecyclerUserAdapter.OnItemClickListener {
            override fun onItemClick(data: Board, pos: Int) {
                val intent = Intent(requireContext(), Posting::class.java)
                intent.putExtra("Title", data.title)
                intent.putExtra("Contents", data.contents)
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
            val title = data?.getStringExtra("Title")
            val contents = data?.getStringExtra("Contents")

            Log.d("ITM", "Received Title: $title, Contents: $contents")

            if (!title.isNullOrBlank() && !contents.isNullOrBlank()) {
                list.add(Board(ContextCompat.getDrawable(requireContext(), R.drawable.user)!!, title, contents))
                adapter.notifyDataSetChanged()
            }
        } else {
            Log.d("ITM", "Invalid requestCode or resultCode: requestCode=$requestCode, resultCode=$resultCode")
        }
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