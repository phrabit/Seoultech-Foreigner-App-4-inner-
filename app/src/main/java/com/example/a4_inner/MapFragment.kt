package com.example.a4_inner

//import android.R
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.a4_inner.databinding.FragmentMapBinding
import com.example.a4_inner.databinding.SelectBuildingBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineManager
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TARGET = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var binding: FragmentMapBinding
    lateinit var line_manager: RouteLineManager
    lateinit var line_layer: RouteLineLayer
    lateinit var label_manager: LabelManager
    lateinit var label_layer: LabelLayer
    lateinit var marker_style: LabelStyles
    private var building_selected: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(TARGET)
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
            showDialog()
        }

        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
//                Log.d("map", "error:" + error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                Log.d("map", "successfully!")
                line_manager = kakaoMap.routeLineManager!!
                line_layer = line_manager.getLayer()
                label_manager = kakaoMap.labelManager!!
                label_layer = label_manager.getLayer()!!
                marker_style = LabelStyles.from(LabelStyle.from(R.drawable.map_marker).setApplyDpScale(false))

//                for(node in UniversitySites.nodes){
//                    label_layer.addLabel(LabelOptions.from(node.location).setStyles(marker_style))
//                }
                if(param1 != null){
                    performActionBasedOnSelection(param1.toString())
                }
            }

            override fun getPosition(): LatLng {
                return LatLng.from(37.631122, 127.077547)
            }

            override fun getZoomLevel(): Int {
                return 17
            }


        })
        return binding.root
    }
    private fun showDialog() {
        val items = arrayOf("Building",
            "(1) Administration Bldg", "(2) Dasan Hall", "(3) Changhak Hall",
            "(5) Hyeseong Hall", "(6) Cheongun Hall", "(7) Seoul Technopark",
            "(8) Graduate Schools", "(10) Power Plant", "(11) Bungeobang Pond",
            "(13) Main Gate", "(30) SeoulTech Daycare Center", "(31) Business Incubation Center",
            "(32) Frontier Hall", "(33) Hi-Tech Hall", "(34) Central Library",
            "(35) Central Library Annex", "(36) Suyeon Hall", "(37) Student Union Bldg",
            "(38) Language Center", "(39) Davinci Hall", "(40) Eoui Hall", "(41) Buram Dormitory",
            "(42) KB Dormitory", "(43) Seongrim Dormitory", "(44) Hyeopdong Gate",
            "(45) Surim Dormitory", "(46) Nuri Dormitory", "(51) The 100th Memorial Hall",
            "(52) Student Union Bldg. 2", "(53) Sangsang Hall", "(54) Areum Hall",
            "(55) University Gymnasium", "(56) Daeryuk Hall", "(57) Mugung Hall",
            "(58) Power Plant 2", "(60) Mirae Hall", "(61) Changeui Gate",
            "(62) Techno Cube", "(63) Main Playground")
        val builder = AlertDialog.Builder(this.requireContext())
        val dialogBinding = SelectBuildingBinding.inflate(layoutInflater)
        val spinner = dialogBinding.spinner

        val adapter = ArrayAdapter<String>(this.requireContext(), androidx.appcompat.R.layout.select_dialog_item_material, items)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nothing selected
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                building_selected = items[position]
            }
        }
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("OK") { _, _ ->
            performActionBasedOnSelection(building_selected)
            PreferenceHelper.setRecentDestinations(this.requireContext(), building_selected)
        }
        builder.setNegativeButton("Cancle"){ _, _ ->

        }
        builder.show()
    }
    public fun performActionBasedOnSelection(itemSelected: String) {
//        val stylesSet: RouteLineStylesSet = RouteLineStylesSet.from(
//            "blueStyles",
//            RouteLineStyles.from(RouteLineStyle.from(30.toFloat(), Color.BLUE))
//        )
//        for(edge in UniversityEdges.edges){
//            var latlngs:MutableList<LatLng> = mutableListOf()
//            latlngs.add(edge.to.location)
//            latlngs.add(edge.from.location)
//            val segment = RouteLineSegment.from(
//                latlngs
//            )
//                .setStyles(stylesSet.getStyles(0))
//
//            val options = RouteLineOptions.from(segment)
//                .setStylesSet(stylesSet)
//
//            val routeLine = line_layer.addRouteLine(options)
//        }

        line_layer.removeAll()
        val crt_latlng = LatLng.from(37.630293, 127.076929)
        var target: Node? = null
        for(node in UniversitySites.nodes){
            if(node.name == itemSelected) target = node
        }
        if(target == null) return
        val path = Dijkstra.dijkstra(crt_latlng, target!!, UniversityEdges.edges)
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

        val routeLine = line_layer.addRouteLine(options)
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
        fun newInstance(target: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(TARGET, target)
                }
            }
    }
}