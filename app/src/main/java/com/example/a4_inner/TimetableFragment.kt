package com.example.a4_inner

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.a4_inner.databinding.ActivityMainBinding
import com.example.a4_inner.databinding.FragmentTimetableBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimetableFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimetableFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentTimetableBinding


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
        binding = FragmentTimetableBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener {

            // 입력할 수 있는 팝업창이 나오도록 하기
            showAddClassDialog()

        }
        return binding.root
    }

    private fun showAddClassDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_class, null)

        builder.setView(dialogView)

        val editClassName = dialogView.findViewById<EditText>(R.id.editClassName)
        val spinnerClassRoom = dialogView.findViewById<Spinner>(R.id.spinnerClassRoom)

        // Spinner에 강의실 목록 추가 (이 부분은 프로젝트에 맞게 수정해야 함)
        val classRoomList = arrayOf("Classroom 1", "Classroom 2", "Classroom 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, classRoomList)
        spinnerClassRoom.adapter = adapter

        builder.setPositiveButton("Add") { dialog, _ ->
            val className = editClassName.text.toString()
            val selectedClassRoom = spinnerClassRoom.selectedItem.toString()

            // 강의 이름(className)과 선택된 강의실(selectedClassRoom) 사용
            // ...

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }


//        builder.setView(dialogView)
//            .setPositiveButton("Add") { dialog, _ ->
//                // 사용자가 "Add" 버튼을 눌렀을 때 처리할 로직을 여기에 추가
//                val className = dialogView.findViewById<EditText>(R.id.editClassName).text.toString()
//                val spinnerClassRoom = dialogView.findViewById<Spinner>(R.id.spinnerClassRoom)
//
//                // 강의 이름과 강의실을 사용할 수 있음 (className, classRoom 변수 활용)
//
//                dialog.dismiss()
//            }
//            .setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }



        val alertDialog = builder.create()
        alertDialog.show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimetableFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimetableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}