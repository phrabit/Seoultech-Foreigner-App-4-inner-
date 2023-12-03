package com.example.a4_inner

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a4_inner.databinding.FragmentMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import java.util.Arrays


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentMapBinding
    lateinit var layer: RouteLineLayer
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
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)
        val mapView: MapView = binding.mapView

        binding.pathFindBtn.setOnClickListener{
            val crt_latlng = LatLng.from(37.630293, 127.076929)
            val path = Dijkstra.dijkstra(crt_latlng, UniversitySites.node5)
            var latlngs:MutableList<LatLng> = mutableListOf(crt_latlng)
            for(node in path) {
                latlngs.add(node.location)
            }
            val stylesSet: RouteLineStylesSet = RouteLineStylesSet.from(
                "blueStyles",
                    RouteLineStyles.from(RouteLineStyle.from(30.toFloat(), Color.BLUE))
            )

            val segment = RouteLineSegment.from(
                latlngs
            )
                .setStyles(stylesSet.getStyles(0))

            val options = RouteLineOptions.from(segment)
                .setStylesSet(stylesSet)

            val routeLine = layer.addRouteLine(options)
        }

        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("map", "error:" + error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                Log.d("map", "successfully!")
                layer = kakaoMap.getRouteLineManager()!!.getLayer()
            }

            override fun getPosition(): LatLng {
                return LatLng.from(37.631122, 127.077547);
            }

            override fun getZoomLevel(): Int {
                return 18
            }


        })
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}