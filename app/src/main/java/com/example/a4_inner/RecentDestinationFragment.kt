package com.example.a4_inner

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnDetach
import androidx.fragment.app.FragmentManager
import com.example.a4_inner.databinding.FragmentRecentDestinationBinding
import com.example.a4_inner.databinding.FragmentTimetableBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG_MAP = "map_fragment"

/**
 * A simple [Fragment] subclass.
 * Use the [RecentDestinationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecentDestinationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentRecentDestinationBinding

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
        binding = FragmentRecentDestinationBinding.inflate(inflater, container, false)
        redraw()
        return binding.root
    }

    fun redraw() {
        super.onResume()
        val recent_buildings = PreferenceHelper.getRecentDestinations(this.requireContext())
        if(recent_buildings[0] != null){
            binding.recentDestination1Btn.text = recent_buildings[0]
            binding.recentDestination1Btn.setOnClickListener {
                var map_fragment = requireActivity().supportFragmentManager.findFragmentByTag(TAG_MAP)
                if(map_fragment == null){
                    map_fragment = MapFragment.newInstance(binding.recentDestination1Btn.text.toString())
                }
                else{
                    (map_fragment as? MapFragment)?.performActionBasedOnSelection(binding.recentDestination1Btn.text.toString())
                }
                (requireActivity() as? NaviActivity)?.setFragment(TAG_MAP, map_fragment)
            }
        }
        else{
            binding.recentDestination1Btn.text = "No building"
        }
        if(recent_buildings[1] != null){
            binding.recentDestination2Btn.text = recent_buildings[1]
            binding.recentDestination2Btn.setOnClickListener {
                var map_fragment = requireActivity().supportFragmentManager.findFragmentByTag(TAG_MAP)
                if(map_fragment == null){
                    map_fragment = MapFragment.newInstance(binding.recentDestination2Btn.text.toString())
                }
                else{
                    (map_fragment as? MapFragment)?.performActionBasedOnSelection(binding.recentDestination2Btn.text.toString())
                }
                (requireActivity() as? NaviActivity)?.setFragment(TAG_MAP, map_fragment!!)
            }
        }
        else{
            binding.recentDestination2Btn.text = "No building"
        }
        if(recent_buildings[2] != null){
            binding.recentDestination3Btn.text = recent_buildings[2]
            binding.recentDestination3Btn.setOnClickListener {
                var map_fragment = requireActivity().supportFragmentManager.findFragmentByTag(TAG_MAP)
                if(map_fragment == null){
                    map_fragment = MapFragment.newInstance(binding.recentDestination3Btn.text.toString())
                }
                else{
                    (map_fragment as? MapFragment)?.performActionBasedOnSelection(binding.recentDestination3Btn.text.toString())
                }
                (requireActivity() as? NaviActivity)?.setFragment(TAG_MAP, map_fragment!!)
            }
        }
        else{
            binding.recentDestination3Btn.text = "No building"
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecentDestinationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecentDestinationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}