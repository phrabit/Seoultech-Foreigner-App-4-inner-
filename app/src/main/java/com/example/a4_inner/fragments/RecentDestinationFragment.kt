package com.example.a4_inner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a4_inner.FragmentTags
import com.example.a4_inner.PreferenceHelper
import com.example.a4_inner.activities.NaviActivity
import com.example.a4_inner.databinding.FragmentRecentDestinationBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
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
                var map_fragment = requireActivity().supportFragmentManager.findFragmentByTag(
                    FragmentTags.TAG_MAP
                )
                if(map_fragment == null){
                    map_fragment = MapFragment.newInstance(binding.recentDestination1Btn.text.toString())
                }
                else{
                    (map_fragment as? MapFragment)?.fetchLocation(binding.recentDestination1Btn.text.toString())
                }
                (requireActivity() as? NaviActivity)?.setFragment(FragmentTags.TAG_MAP, map_fragment)
            }
        }
        else{
            binding.recentDestination1Btn.text = "No building"
        }
        if(recent_buildings[1] != null){
            binding.recentDestination2Btn.text = recent_buildings[1]
            binding.recentDestination2Btn.setOnClickListener {
                var map_fragment = requireActivity().supportFragmentManager.findFragmentByTag(
                    FragmentTags.TAG_MAP
                )
                if(map_fragment == null){
                    map_fragment = MapFragment.newInstance(binding.recentDestination2Btn.text.toString())
                }
                else{
                    (map_fragment as? MapFragment)?.fetchLocation(binding.recentDestination2Btn.text.toString())
                }
                (requireActivity() as? NaviActivity)?.setFragment(FragmentTags.TAG_MAP, map_fragment!!)
            }
        }
        else{
            binding.recentDestination2Btn.text = "No building"
        }
        if(recent_buildings[2] != null){
            binding.recentDestination3Btn.text = recent_buildings[2]
            binding.recentDestination3Btn.setOnClickListener {
                var map_fragment = requireActivity().supportFragmentManager.findFragmentByTag(
                    FragmentTags.TAG_MAP
                )
                if(map_fragment == null){
                    map_fragment = MapFragment.newInstance(binding.recentDestination3Btn.text.toString())
                }
                else{
                    (map_fragment as? MapFragment)?.fetchLocation(binding.recentDestination3Btn.text.toString())
                }
                (requireActivity() as? NaviActivity)?.setFragment(FragmentTags.TAG_MAP, map_fragment!!)
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