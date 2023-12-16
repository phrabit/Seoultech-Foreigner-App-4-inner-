package com.example.a4_inner

import com.example.a4_inner.UniversityEdges.edges
import com.example.a4_inner.UniversitySites.nodes
import com.kakao.vectormap.LatLng

data class Node(val name: String, val location: LatLng)

data class Edge(val from: Node, val to: Node) {
    val weight: Double = getDistance(from.location, to.location)
}

fun getDistance(src: LatLng, dest: LatLng): Double {
    val dx = src.latitude - dest.latitude
    val dy = src.longitude - dest.longitude
    return Math.sqrt((dx * dx + dy * dy).toDouble())
}

object UniversitySites {
    val main_gate = Node("(13) Main Gate", LatLng.from(37.630181, 127.076815))
    val rotary1 = Node("rotary1", LatLng.from(37.631114, 127.077560))
    val rotary2 = Node("rotary2", LatLng.from(37.631788, 127.078471))
    val daycare_center = Node("(30) SeoulTech Daycare Center", LatLng.from(37.630770, 127.076557))
    val frontier_hall = Node("(32) Frontier Hall", LatLng.from(37.631171, 127.076598))
    val business_incubation_center = Node("(31) Business Incubation Center", LatLng.from(37.631031, 127.075718))
    val hightech_hall = Node("(33) Hi-Tech Hall", LatLng.from(37.631947, 127.076876))
    val hyanghacro_exit = Node("hyanghacro_exit", LatLng.from(37.633124, 127.077468))
    val library = Node("(34) Central Library", LatLng.from(37.633276, 127.076878))
    val hightech_hall_right_corner = Node("hightech_hall_right_corner", LatLng.from(37.632446, 127.077037))
    val hightech_hall_left_corner = Node("hightech_hall_left_corner", LatLng.from(37.632646, 127.076287))
    val library_smoke = Node("library_smoke", LatLng.from(37.633428, 127.076214))
    val nuri_dormitory = Node("(46) Nuri Dormitory", LatLng.from(37.634626, 127.076632))
    val byway_library = Node("byway_library", LatLng.from(37.634059, 127.076420))
    val student_bldg_1 = Node("(37) Student Union Bldg", LatLng.from(37.633943, 127.077037))
    val library_annex = Node("(35) Central Library Annex", LatLng.from(37.633591, 127.076904))
    val eoui_hall = Node("(40) Eoui Hall", LatLng.from(37.634914, 127.076812))
    val seongrim_dormitary = Node("(43) Seongrim Dormitory", LatLng.from(37.635153, 127.076348))
    val seongrim_dormitary_corner = Node("seongrim_dormitary_corner", LatLng.from(37.635024, 127.075320))
    val buram_dormitory = Node("(41) Buram Dormitory", LatLng.from(37.636054, 127.076164))
    val hyeopdong_gate = Node("(44) Hyeopdong Gate", LatLng.from(37.636127, 127.075735))
    val kb_dormitory = Node("(42) KB Dormitory", LatLng.from(37.635661, 127.076467))
    val surim_entrance = Node("surim_entance", LatLng.from(37.635724, 127.077004))
    val surim_dormitory = Node( "(45) Surim Dormitory", LatLng.from(37.636124, 127.078090))
    val language_center = Node("(38) Language Center", LatLng.from(37.634738, 127.077414))
    val davinci_hall = Node("(39) Davinci Hall", LatLng.from(37.634551, 127.078094))
    val graduate_school = Node("(8) Graduate Schools", LatLng.from(37.634492, 127.079341))
    val bungeobang_bus_stop = Node("bungeobang_bus_stop", LatLng.from(37.634064, 127.077959))
    val power_plant_1 = Node("(10) Power Plant", LatLng.from(37.633518, 127.078945))
    val hill_from_power_plant_1 = Node("hill_from_power_plant_1", LatLng.from(37.634212, 127.079680))
    val techno_park = Node("(7) Seoul Technopark", LatLng.from(37.634251, 127.080442))
    val bungeobang = Node("(11) Bungeobang Pond", LatLng.from(37.633235, 127.078404))
    val bungeobang_threeway_intersection = Node("bungeobang_threeway_intersection", LatLng.from(37.632726, 127.078801))
    val dasan_bus_stop = Node("bungeobang_threeway_intersection", LatLng.from(37.632206, 127.078631))
    val dasan_hall = Node("(2) Dasan Hall", LatLng.from(37.631979, 127.077856))
    val changhak_hall = Node("(3) Changhak Hall", LatLng.from(37.632005, 127.079345))
    val stair_front_changhak_hall = Node("stair_front_changhak_hall", LatLng.from(37.631621, 127.079162))
    val cheongun_hall = Node("(6) Cheongun Hall", LatLng.from(37.633188, 127.080674))
    val stream_threeway_intersection = Node("stream_threeway_intersection", LatLng.from(37.631283, 127.079573))
    val administration_bldg = Node("(1) Administration Bldg", LatLng.from(37.631620, 127.080324))
    val hyeseong_hall = Node("(5) Hyeseong Hall", LatLng.from(37.631423, 127.081851))
    val changeui_gate = Node("(61) Changeui Gate", LatLng.from(37.631478, 127.082943))
    val power_plant_2 = Node("(58) Power Plant 2", LatLng.from(37.631009, 127.081826))
    val mugung_hall = Node("(57) Mugung Hall", LatLng.from(37.631142, 127.080926))
    val sangsang_hall = Node("(53) Sangsang Hall", LatLng.from(37.631082, 127.079660))
    val student_bldg_2 = Node("(52) Student Union Bldg. 2", LatLng.from(37.630696, 127.079533))
    val memorial_hall = Node("(51) The 100th Memorial Hall", LatLng.from(37.630418, 127.078321))
    val daeryuk_hall = Node("(56) Daeryuk Hall", LatLng.from(37.629125, 127.080327))
    val mirae_hall = Node("(60) Mirae Hall", LatLng.from(37.629492, 127.080926))
    val playground = Node("(63) Main Playground", LatLng.from(37.629757, 127.078333))
    val techno_cube = Node("(62) Techno Cube", LatLng.from(37.630354, 127.079490))
    val techno_threeway_intersection = Node("techno_threeway_intersection", LatLng.from(37.629589, 127.080130))
    val gym = Node("(55) University Gymnasium", LatLng.from(37.629523, 127.079532))
    val sangsang_mugung_threeway_intersection = Node("sangsang_mugung_threeway_intersection", LatLng.from(37.631162, 127.080540))
    val areum_hall = Node("(54) Areum Hall", LatLng.from(37.630113, 127.080275))

    val nodes:List<Node> = listOf(
        main_gate, rotary1, rotary2, daycare_center, frontier_hall, business_incubation_center, hightech_hall,
        hyanghacro_exit, library, hightech_hall_right_corner, hightech_hall_left_corner, library_smoke,
        nuri_dormitory, byway_library, student_bldg_1, library_annex, eoui_hall, seongrim_dormitary,
        seongrim_dormitary_corner, buram_dormitory, hyeopdong_gate, kb_dormitory, surim_entrance,stair_front_changhak_hall,
        surim_dormitory, language_center, davinci_hall, graduate_school, bungeobang_bus_stop, power_plant_1,
        hill_from_power_plant_1, techno_park, bungeobang, bungeobang_threeway_intersection,dasan_bus_stop,
        dasan_hall, changhak_hall, cheongun_hall, stream_threeway_intersection, administration_bldg,techno_threeway_intersection,
        hyeseong_hall, changeui_gate, power_plant_2, mugung_hall, sangsang_hall, student_bldg_2, areum_hall,
        memorial_hall, daeryuk_hall, mirae_hall, playground, techno_cube, gym, sangsang_mugung_threeway_intersection,
    )
}

object UniversityEdges{
    val edge1 = Edge(UniversitySites.main_gate, UniversitySites.rotary1)
    val edge2 = Edge(UniversitySites.main_gate, UniversitySites.daycare_center)
    val edge3 = Edge(UniversitySites.daycare_center, UniversitySites.frontier_hall)
    val edge4 = Edge(UniversitySites.frontier_hall, UniversitySites.hightech_hall)
    val edge5 = Edge(UniversitySites.hightech_hall, UniversitySites.hightech_hall_right_corner)
    val edge6 = Edge(UniversitySites.hightech_hall_right_corner, UniversitySites.hightech_hall_left_corner)
    val edge7 = Edge(UniversitySites.hightech_hall_left_corner, UniversitySites.library_smoke)
    val edge8 = Edge(UniversitySites.library_smoke, UniversitySites.library)
    val edge9 = Edge(UniversitySites.library_smoke, UniversitySites.byway_library)
    val edge10 = Edge(UniversitySites.byway_library, UniversitySites.student_bldg_1)
    val edge11 = Edge(UniversitySites.student_bldg_1, UniversitySites.library_annex)
    val edge12 = Edge(UniversitySites.library_annex, UniversitySites.library)
    val edge13 = Edge(UniversitySites.student_bldg_1, UniversitySites.hyanghacro_exit)
    val edge14 = Edge(UniversitySites.library, UniversitySites.hyanghacro_exit)
    val edge15 = Edge(UniversitySites.student_bldg_1, UniversitySites.library_annex)
    val edge16 = Edge(UniversitySites.byway_library, UniversitySites.nuri_dormitory)
    val edge17 = Edge(UniversitySites.nuri_dormitory, UniversitySites.eoui_hall)
    val edge18 = Edge(UniversitySites.nuri_dormitory, UniversitySites.seongrim_dormitary)
    val edge19 = Edge(UniversitySites.nuri_dormitory, UniversitySites.seongrim_dormitary_corner)
    val edge20 = Edge(UniversitySites.seongrim_dormitary, UniversitySites.kb_dormitory)
    val edge21 = Edge(UniversitySites.kb_dormitory, UniversitySites.surim_entrance)
    val edge22 = Edge(UniversitySites.kb_dormitory, UniversitySites.buram_dormitory)
    val edge23 = Edge(UniversitySites.buram_dormitory, UniversitySites.hyeopdong_gate)
    val edge24 = Edge(UniversitySites.seongrim_dormitary_corner, UniversitySites.hyeopdong_gate)
    val edge25 = Edge(UniversitySites.eoui_hall, UniversitySites.surim_entrance)
    val edge26 = Edge(UniversitySites.eoui_hall, UniversitySites.language_center)
    val edge27 = Edge(UniversitySites.language_center, UniversitySites.davinci_hall)
    val edge28 = Edge(UniversitySites.davinci_hall, UniversitySites.bungeobang_bus_stop)
    val edge29 = Edge(UniversitySites.davinci_hall, UniversitySites.graduate_school)
    val edge30 = Edge(UniversitySites.bungeobang_bus_stop, UniversitySites.power_plant_1)
    val edge31 = Edge(UniversitySites.bungeobang_bus_stop, UniversitySites.bungeobang)
    val edge32 = Edge(UniversitySites.power_plant_1, UniversitySites.bungeobang)
    val edge33 = Edge(UniversitySites.bungeobang_threeway_intersection, UniversitySites.bungeobang)
    val edge34 = Edge(UniversitySites.hyanghacro_exit, UniversitySites.bungeobang)
    val edge35 = Edge(UniversitySites.bungeobang_bus_stop, UniversitySites.bungeobang)
    val edge36 = Edge(UniversitySites.power_plant_1, UniversitySites.hill_from_power_plant_1)
    val edge37 = Edge(UniversitySites.hill_from_power_plant_1, UniversitySites.techno_park)
    val edge38 = Edge(UniversitySites.techno_park, UniversitySites.cheongun_hall)
    val edge39 = Edge(UniversitySites.bungeobang_threeway_intersection, UniversitySites.dasan_bus_stop)
    val edge40 = Edge(UniversitySites.dasan_bus_stop, UniversitySites.rotary2)
    val edge41 = Edge(UniversitySites.rotary2, UniversitySites.dasan_hall)
    val edge42 = Edge(UniversitySites.dasan_hall, UniversitySites.rotary1)
    val edge43 = Edge(UniversitySites.rotary1, UniversitySites.rotary2)
    val edge44 = Edge(UniversitySites.dasan_bus_stop, UniversitySites.changhak_hall)
    val edge45 = Edge(UniversitySites.changhak_hall, UniversitySites.cheongun_hall)
    val edge46 = Edge(UniversitySites.rotary2, UniversitySites.stair_front_changhak_hall)
    val edge47 = Edge(UniversitySites.stair_front_changhak_hall, UniversitySites.changhak_hall)
    val edge48 = Edge(UniversitySites.stair_front_changhak_hall, UniversitySites.stream_threeway_intersection)
    val edge49 = Edge(UniversitySites.rotary1, UniversitySites.stream_threeway_intersection)
    val edge50 = Edge(UniversitySites.stream_threeway_intersection, UniversitySites.sangsang_hall)
    val edge51 = Edge(UniversitySites.stream_threeway_intersection, UniversitySites.sangsang_mugung_threeway_intersection)
    val edge52 = Edge(UniversitySites.sangsang_mugung_threeway_intersection, UniversitySites.mugung_hall)
    val edge53 = Edge(UniversitySites.mugung_hall, UniversitySites.power_plant_2)
    val edge54 = Edge(UniversitySites.power_plant_2, UniversitySites.hyeseong_hall)
    val edge55 = Edge(UniversitySites.hyeseong_hall, UniversitySites.changeui_gate)
    val edge56 = Edge(UniversitySites.stream_threeway_intersection, UniversitySites.hyeseong_hall)
    val edge57 = Edge(UniversitySites.stream_threeway_intersection, UniversitySites.administration_bldg)
    val edge58 = Edge(UniversitySites.sangsang_hall, UniversitySites.student_bldg_2)
    val edge59 = Edge(UniversitySites.student_bldg_2, UniversitySites.techno_cube)
    val edge60 = Edge(UniversitySites.techno_cube, UniversitySites.gym)
    val edge61 = Edge(UniversitySites.gym, UniversitySites.daeryuk_hall)
    val edge62 = Edge(UniversitySites.gym, UniversitySites.techno_threeway_intersection)
    val edge63 = Edge(UniversitySites.techno_threeway_intersection, UniversitySites.areum_hall)
    val edge64 = Edge(UniversitySites.areum_hall, UniversitySites.sangsang_mugung_threeway_intersection)
    val edge65 = Edge(UniversitySites.techno_threeway_intersection, UniversitySites.mirae_hall)
    val edge66 = Edge(UniversitySites.daycare_center, UniversitySites.business_incubation_center)
    val edge67 = Edge(UniversitySites.business_incubation_center, UniversitySites.hightech_hall_left_corner)
    val edge68 = Edge(UniversitySites.surim_entrance, UniversitySites.surim_dormitory)
    val edge69 = Edge(UniversitySites.daycare_center, UniversitySites.hyanghacro_exit)

    val edges:List<Edge> = listOf(
        edge1, edge2, edge3, edge4, edge5, edge6, edge7, edge8, edge9, edge10,
        edge11, edge12, edge13, edge14, edge15, edge16, edge17,edge18, edge19, edge20,
        edge21, edge22, edge23, edge24, edge25, edge26, edge27,edge28, edge29, edge30,
        edge31, edge32, edge33, edge34, edge35, edge36, edge37, edge38, edge39, edge40,
        edge41, edge42, edge43, edge44, edge45, edge46, edge47, edge48, edge49, edge50,
        edge51, edge52, edge53, edge54, edge55, edge56, edge57, edge58, edge59, edge60,
        edge61, edge62, edge63, edge64, edge65, edge66, edge67, edge68, edge69,
    )
}

object Dijkstra {
//    fun dijkstra(currentLocation: LatLng, end: Node): List<Node> {
//        // 현재 위치에서 가장 가까운 노드를 찾습니다.
//        val start = nodes.minByOrNull { getDistance(currentLocation, it.location) }!!
//
//        val dist = nodes.associateWith { Double.MAX_VALUE }.toMutableMap()
//        val previous = nodes.associateWith { null }.toMutableMap<Node, Node?>()
//
//        dist[start] = 0.0
//        val unvisited = nodes.toMutableList()
//
//        while (unvisited.isNotEmpty()) {
//            val current = unvisited.minByOrNull { dist[it]!! }!!
//            unvisited.remove(current)
//
//            val currentEdges = edges.filter { it.from == current }
//            for (edge in currentEdges) {
//                val alt = dist[current]!! + edge.weight
//                if (alt < dist[edge.to]!!) {
//                    dist[edge.to] = alt
//                    previous[edge.to] = current
//                }
//            }
//        }
//
//        // 경로 생성
//        val path = mutableListOf<Node>()
//        var current: Node? = end
//        while (current != null) {
//            path.add(current)
//            current = previous[current]
//        }
//        path.reverse()
//
//        return path
//    }
fun dijkstra(currentLocation: LatLng, end: Node, edges: List<Edge>): List<Node> {
    val nodes = edges.flatMap { listOf(it.from, it.to) }.distinct()

    // 현재 위치에서 가장 가까운 노드를 찾습니다.
    val start = nodes.minByOrNull { getDistance(currentLocation, it.location) }!!

    val dist = nodes.associateWith { Double.MAX_VALUE }.toMutableMap()
    val previous = nodes.associateWith { null }.toMutableMap<Node, Node?>()

    dist[start] = 0.0
    val unvisited = nodes.toMutableList()

    while (unvisited.isNotEmpty()) {
        val current = unvisited.minByOrNull { dist[it]!! }!!
        unvisited.remove(current)

        val currentEdges = edges.filter { it.from == current || it.to == current }
        for (edge in currentEdges) {
            val neighbor = if (edge.from == current) edge.to else edge.from
            val alt = dist[current]!! + edge.weight
            if (alt < dist[neighbor]!!) {
                dist[neighbor] = alt
                previous[neighbor] = current
            }
        }
    }

    // 경로 생성
    val path = mutableListOf<Node>()
    var current: Node? = end
    while (current != null) {
        path.add(current)
        current = previous[current]
    }
    path.reverse()

    return path
}
}