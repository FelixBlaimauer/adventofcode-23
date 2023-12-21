package day_10

import OpposingRectDirection
import RectDirection
import Vec2
import getResourceFile
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

val PipeFitment = mapOf(
    '-' to setOf(RectDirection.LEFT, RectDirection.RIGHT),
    '|' to setOf(RectDirection.UP, RectDirection.DOWN),
    'S' to RectDirection.entries.toSet(),
    'L' to setOf(RectDirection.UP, RectDirection.RIGHT),
    'J' to setOf(RectDirection.UP, RectDirection.LEFT),
    '7' to setOf(RectDirection.DOWN, RectDirection.LEFT),
    'F' to setOf(RectDirection.DOWN, RectDirection.RIGHT),
    '.' to setOf()
)

fun pipeFits(a: Char, b: Char, direction: RectDirection) =
    direction in PipeFitment[a]!! && OpposingRectDirection[direction] in PipeFitment[b]!!

fun shoelace(points: List<Vec2>): Double {
    val topRow = points.map { it.x }
    val bottomRow = points.map { it.y }

    var area = 0

    for (i in topRow.indices) {
        area += abs(topRow[i % topRow.size] * bottomRow[(i + 1) % topRow.size] - bottomRow[i % topRow.size] * topRow[(i + 1) % topRow.size])
    }

    val perimeter = points.zipWithNext().fold(0) { acc, cur ->
        val (second, first) = cur
        acc + abs((second.y - first.y) + (second.x - first.x))
    }
    println(perimeter)
    return area / 2.0 - perimeter
}

fun main() {
    var start: Vec2? = null
    val grid = getResourceFile("day_10/example3.txt").readLines().mapIndexed { y, line ->
        line.toCharArray().mapIndexed { x, it ->
            if (it == 'S') start = Vec2(x, y)
            it
        }
    }

    var current = start ?: throw Exception("Start not found")
    val path = mutableSetOf<Vec2>()
    pathFind@ while (grid[current.y][current.x] != 'S' || path.size < 3) {
        val neighbors = current.getSafeRectNeighbors().filterValues { it.y < grid.size && it.x < grid[0].size };
        val nextPiece =
            neighbors.filterValues { if (path.size < 4) it != start else true }.entries.find { (direction, neighbor) ->
                neighbor !in path && pipeFits(
                    grid[current.y][current.x], grid[neighbor.y][neighbor.x], direction
                )
            } ?: throw Exception("No fitting neighbor")
        current = nextPiece.value
        path += current
    }

    println("Farthest distance: ${Math.round(path.size / 2f)}")
    println("Contained area: ${shoelace(path.toList())}")
}