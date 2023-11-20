package com.example.a4_inner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CourseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var yearAdapter: ArrayAdapter<*>
    private lateinit var yearSpinner: Spinner
    private lateinit var termAdapter: ArrayAdapter<*>
    private lateinit var termSpinner: Spinner
    private lateinit var areaAdapter: ArrayAdapter<*>
    private lateinit var areaSpinner: Spinner
    private lateinit var majorAdapter: ArrayAdapter<*>
    private lateinit var majorSpinner: Spinner

    private var courseUniversity: String = ""
    private var courseYear: String = ""
    private var courseTerm: String = ""
    private var courseArea: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseUniversityGroup = getView()?.findViewById<RadioGroup>(R.id.courseUniversityGroup)
        yearSpinner = getView()?.findViewById(R.id.yearSpinner) as Spinner
        termSpinner = getView()?.findViewById(R.id.termSpinner) as Spinner
        areaSpinner = getView()?.findViewById(R.id.areaSpinner) as Spinner
        majorSpinner = getView()?.findViewById(R.id.majorSpinner) as Spinner

        courseUniversityGroup?.setOnCheckedChangeListener { _, i ->
            val courseButton = getView()?.findViewById<RadioButton>(i)
            courseUniversity = courseButton?.text.toString()

            yearAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.year, android.R.layout.simple_spinner_dropdown_item)
            yearSpinner.adapter = yearAdapter

            termAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.term, android.R.layout.simple_spinner_dropdown_item)
            termSpinner.adapter = termAdapter

            when (courseUniversity) {
                "학부" -> {
                    areaAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.universityArea, android.R.layout.simple_spinner_dropdown_item)
                    areaSpinner.adapter = areaAdapter
                    majorAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.universityRefinementMajor, android.R.layout.simple_spinner_dropdown_item)
                    majorSpinner.setAdapter(majorAdapter);
                }
                "대학원" -> {
                    areaAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.graduateArea, android.R.layout.simple_spinner_dropdown_item)
                    areaSpinner.adapter = areaAdapter
                    majorAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.graduateMajor, android.R.layout.simple_spinner_dropdown_item)
                    majorSpinner.setAdapter(majorAdapter);
                }
            }

//        areaSpinner.setOnItemClickListener(new AdapterView.onItemSelectedListener(){
//
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
//                if(areaSpinner.getSelectedItem().equals("교양및기타")){
//                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityRefinementMajor, android.R.layout.simple_spinner_dropdown_item)
//                    majorSpinner.setAdapter(majorAdapter);
//                }
//                if(areaSpinner.getSelectedItem().equals("전공")){
//                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityMajor, android.R.layout.simple_spinner_dropdown_item)
//                    majorSpinner.setAdapter(majorAdapter);
//
//                }
//                if(areaSpinner.getSelectedItem().equals("일반대학원")){
//                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateMajor, android.R.layout.simple_spinner_dropdown_item)
//                    majorSpinner.setAdapter(majorAdapter);
//                }
//            }
//
//        })

            areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                    when (areaSpinner.selectedItem) {
                        "교양및기타" -> {
                            majorAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.universityRefinementMajor, android.R.layout.simple_spinner_dropdown_item)
                            majorSpinner.adapter = majorAdapter
                        }
                        "전공" -> {
                            majorAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.universityMajor, android.R.layout.simple_spinner_dropdown_item)
                            majorSpinner.adapter = majorAdapter
                        }
                        "일반대학원" -> {
                            majorAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.graduateMajor, android.R.layout.simple_spinner_dropdown_item)
                            majorSpinner.adapter = majorAdapter
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CourseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}