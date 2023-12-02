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
    val node1 = Node("1", LatLng.from(37.630851, 127.076502))
    val node2 = Node("2", LatLng.from(37.631483, 127.076710))
    val node3 = Node("3", LatLng.from(37.632443, 127.077041))
    val node4 = Node("4", LatLng.from(37.632608, 127.076229))
    val node5 = Node("5", LatLng.from(37.631665, 127.075907))
    val node6 = Node("6", LatLng.from(37.631034, 127.075710))

    val nodes:List<Node> = listOf(node1, node2, node3, node4, node5, node6)
}

object UniversityEdges{
    val edge1 = Edge(UniversitySites.node1, UniversitySites.node2)
    val edge2 = Edge(UniversitySites.node2, UniversitySites.node3)
    val edge3 = Edge(UniversitySites.node3, UniversitySites.node4)
    val edge4 = Edge(UniversitySites.node4, UniversitySites.node5)
    val edge5 = Edge(UniversitySites.node5, UniversitySites.node6)
    val edge6 = Edge(UniversitySites.node6, UniversitySites.node1)

    val edges:List<Edge> = listOf(edge1, edge2, edge3, edge4, edge5, edge6)
}

object Dijkstra {
    fun dijkstra(currentLocation: LatLng, end: Node): List<Node> {
        // 현재 위치에서 가장 가까운 노드를 찾습니다.
        val start = nodes.minByOrNull { getDistance(currentLocation, it.location) }!!

        val dist = nodes.associateWith { Double.MAX_VALUE }.toMutableMap()
        val previous = nodes.associateWith { null }.toMutableMap<Node, Node?>()

        dist[start] = 0.0
        val unvisited = nodes.toMutableList()

        while (unvisited.isNotEmpty()) {
            val current = unvisited.minByOrNull { dist[it]!! }!!
            unvisited.remove(current)

            val currentEdges = edges.filter { it.from == current }
            for (edge in currentEdges) {
                val alt = dist[current]!! + edge.weight
                if (alt < dist[edge.to]!!) {
                    dist[edge.to] = alt
                    previous[edge.to] = current
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