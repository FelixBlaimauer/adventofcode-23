package day_08

import getResourceFile

data class Node(
    val id: String,
    val leftId: String,
    val rightId: String,
    var leftNode: Node? = null,
    var rightNode: Node? = null
) {
    companion object {
        fun fromString(string: String): Node {
            val id = string.split(" = ")[0]
            val (left, right) = string.split(" = ")[1].replace("[()]".toRegex(), "").split(", ")

            return Node(id, left, right)
        }
    }
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun countSteps(startNode: Node, instructions: String): Long {
    var current = startNode

    var distance = 0L

    while (current.id != "ZZZ") {
        current = if (instructions[(distance % instructions.length).toInt()] == 'R') current.rightNode!! else current.leftNode!!

        distance++
    }
    return distance
}

fun countGhostSteps(startNode: Node, instructions: String): Long {
    var current = startNode

    var distance = 0L

    while (!current.id.endsWith('Z')) {
        current = if (instructions[(distance % instructions.length).toInt()] == 'R') current.rightNode!! else current.leftNode!!

        distance++
    }
    return distance
}

fun main() {
    val input = getResourceFile("day_08/input.txt").readLines()

    val instructions = input[0]

    val grouped = input.subList(2, input.count()).map { Node.fromString(it) }.groupBy { it.id }

    val nodes = HashMap<String, Node>()

    for ((key, value) in grouped) {
        nodes[key] = value[0]
    }

    for (node in nodes.values) {
        node.leftNode = nodes[node.leftId]
        node.rightNode = nodes[node.rightId]
    }

    // Level 1
//    var current = nodes["AAA"]!!
//
//    val distance = countSteps(nodes["AAA"]!!, instructions)
//
//    println("AAA to ZZZ is: $distance")

    // Level 2
    val startNodes = nodes.filter{(id) -> id.endsWith('A')}

    println(startNodes.map{it.key})

    val minDistances = startNodes.values.map { countGhostSteps(it, instructions) }

    println("Got distances")

    val quickestCommon = minDistances.reduce{acc, cur -> findLCM(acc, cur)}
//    val currentNodes = startNodes.associateWith {nodes[it]!!}.toMutableMap()
//
//    while (!currentNodes.all{(_, it) -> it.id.endsWith('Z')}) {
//        for ((key, node) in currentNodes) {
//            currentNodes[key] = if (instructions[(distance % instructions.length).toInt()] == 'R') node.rightNode!! else node.leftNode!!
//        }
//
//        distance++
//        println(distance)
//    }

    println("All **A to only **Z: $quickestCommon")
}