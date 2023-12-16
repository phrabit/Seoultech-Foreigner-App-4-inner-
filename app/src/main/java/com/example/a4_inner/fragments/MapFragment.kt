package com.example.a4_inner.fragments

//import android.R
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.a4_inner.Dijkstra
import com.example.a4_inner.Node
import com.example.a4_inner.PreferenceHelper
import com.example.a4_inner.R
import com.example.a4_inner.UniversityEdges
import com.example.a4_inner.UniversitySites
import com.example.a4_inner.databinding.FragmentMapBinding
import com.example.a4_inner.databinding.SelectBuildingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelManager
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {

            }

            override fun onMapError(error: Exception) {

                Log.d("map", "error:" + error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {

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
                    fetchLocation(param1.toString())
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
        val items = UniversitySites.building_array
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
            fetchLocation(building_selected)
            PreferenceHelper.setRecentDestinations(requireContext(), building_selected)
        }
        builder.setNegativeButton("Cancle"){ _, _ ->

        }
        builder.show()
    }
    public fun drawShortestPath(user_location: MutableList<Double>, itemSelected: String) {
        line_layer.removeAll()
        val crt_latlng = LatLng.from(user_location[0], user_location[1])
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

    public fun fetchLocation(itemSelected: String) {
        var user_location : MutableList<Double>?= mutableListOf<Double>()
        if (ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        val task: Task<Location> = fusedLocationClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                // Use the location object as needed
                // 이 위치를 사용하여 필요한 작업을 수행합니다.
                user_location?.add(location.latitude)
                user_location?.add(location.longitude)
                drawShortestPath(user_location!!, itemSelected)
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