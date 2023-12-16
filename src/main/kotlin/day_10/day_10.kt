package day_10

import OpposingRectDirection
import RectDirection
import Vec2
import getResourceFile
import java.lang.Exception

val charToPipe = mapOf(
    'S' to mapOf(
        RectDirection.UP to true,
        RectDirection.RIGHT to true,
        RectDirection.DOWN to true,
        RectDirection.LEFT to true
    ),
    '-' to mapOf(RectDirection.LEFT to true, RectDirection.RIGHT to true),
    '|' to mapOf(RectDirection.UP to true, RectDirection.DOWN to true),
    'L' to mapOf(RectDirection.UP to true, RectDirection.RIGHT to true),
    '7' to mapOf(RectDirection.DOWN to true, RectDirection.LEFT to true),
    'J' to mapOf(RectDirection.UP to true, RectDirection.LEFT to true),
    'F' to mapOf(RectDirection.DOWN to true, RectDirection.RIGHT to true)
)

data class Pipe(val ends: Map<RectDirection, Boolean>, val letter: Char) {
    companion object {
        fun fromChar(char: Char) = charToPipe[char]?.let { Pipe(it, char) }

    }

    fun fits(pipe: Pipe, direction: RectDirection) =
        this.ends[direction] == true && pipe.ends[OpposingRectDirection[direction]] == true
}

tailrec fun findPipeDistance(current: Vec2, grid: List<List<Pipe?>>, visited: List<Vec2>, distance: Int): Int {
    val neighbor = current.getSafeRectNeighbors()
        .filterValues { it.y < grid.count() && it.x < grid[0].count() }.entries.first { (direction, pos) ->
        grid[pos.y][pos.x]?.let {
            if (it.letter == 'S' && distance > 1) return distance + 1

            return@first pos !in visited && grid[current.y][current.x]!!.fits(it, direction)
        } ?: false
    }.value

    return findPipeDistance(neighbor, grid, visited + current, distance + 1)
}

fun main() {
    var startIdx: Vec2? = null

    val grid = getResourceFile("day_10/example.txt").readLines().mapIndexed { y, line ->
        line.mapIndexed { x, it ->
            if (it == 'S') startIdx = Vec2(x, y)
            Pipe.fromChar(it)
        }
    }

    val start = startIdx ?: throw Exception("No start found")

    val n = start.getSafeRectNeighbors().entries.first { (direction, pos) ->
        grid[pos.y][pos.x]?.let {
            grid[start.y][start.x]!!.fits(
                it,
                direction
            )
        } ?: false
    }.value

    val distance = findPipeDistance(n, grid, listOf(start), 1)

    println("Loop is $distance long")
    println("Farthest element: ${Math.round(distance / 2f)}")
}