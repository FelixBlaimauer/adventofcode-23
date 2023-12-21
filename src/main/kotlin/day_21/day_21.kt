package day_21

import Vec2
import getResourceFile

const val STEPS = 7

data class Node(val pos: Vec2, val walkable: Boolean = true) {
    val neighbors = mutableSetOf<Node>()
}

fun findReachableTiles(nodes: Set<Node>, steps: Int): Set<Node> {
    val reachable = mutableSetOf<Node>()

    for (current in nodes) {
        val neighbors = current.neighbors.filter { it.walkable }
        reachable += neighbors
    }

    return if (steps == 1) reachable else findReachableTiles(reachable, steps-1)
}

fun main() {
    var startPos: Pair<Int, Int>? = null

    val nodes = getResourceFile("day_21/input.txt").readLines().mapIndexed { y, line ->
        line.toCharArray().mapIndexed { x, it ->
            if (it == 'S') startPos = x to y
            Node(Vec2(x, y), it != '#')
        }
    }

    for (node in nodes.flatten()) {
        val neighbors = node.pos.getSafeRectNeighbors().values.filter { it.y < nodes.size && it.x < nodes[0].size }
        node.neighbors += neighbors.map { nodes[it.y][it.x] }
    }

    val startNode =
        if (startPos == null) throw Exception("No start found") else nodes[startPos!!.second][startPos!!.first]

    val reachable = findReachableTiles(setOf(startNode), 64)
    println("Reachable tiles: ${reachable.size}")

//    for (row in nodes) {
//        for (node in row) {
//            if (node == startNode) print("S")
//            else if (node in reachable) print("O")
//            else if (node.walkable) print(".")
//            else print("#")
//        }
//        println()
//    }
}