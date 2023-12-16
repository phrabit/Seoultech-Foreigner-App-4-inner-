package com.example.a4_inner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a4_inner.databinding.FragmentRecentDestinationBinding
import com.example.a4_inner.databinding.FragmentRecentPostBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TAG_HOME = "home_fragment"
private const val TAG_BULLETIN = "bulletin_fragment"
/**
 * A simple [Fragment] subclass.
 * Use the [RecentPostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecentPostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentRecentPostBinding
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
        binding = FragmentRecentPostBinding.inflate(inflater, container, false)
        fetchRecentBulletinData(requireParentFragment() as? HomeFragment)
        return binding.root
    }

    public fun refresh(){
        val recent_post_txt_list = listOf(binding.recentPost1Txt, binding.recentPostTxt2, binding.recentPostTxt3)
        val recent_posts = RecentBulletinData.getRecentBulletinList()
        for((post, ui) in recent_posts!!.zip(recent_post_txt_list)){
            ui.text = post.title
            ui.setOnClickListener {
//                val bulletin_fragment = requireActivity().supportFragmentManager.findFragmentByTag(
//                    TAG_BULLETIN)
                (requireActivity() as? NaviActivity)?.setFragment(TAG_BULLETIN, BulletinFragment())
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecentPostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecentPostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}