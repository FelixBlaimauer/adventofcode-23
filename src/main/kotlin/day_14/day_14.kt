package day_14

import RectDirection
import RectDirectionVectors
import Vec2
import getResourceFile

const val TILT_CYCLES = 1000000000

enum class Rock {
    ROUND, CUBE
}

val CharToRock = mapOf(
    'O' to Rock.ROUND, '#' to Rock.CUBE
)

fun tilt(g: MutableList<MutableList<Rock?>>, direction: RectDirection): MutableList<MutableList<Rock?>> {
    var grid = g

    val vec = RectDirectionVectors[RectDirection.UP]!!
//    val vec = when (direction) {
//        RectDirection.UP, RectDirection.DOWN -> RectDirectionVectors[RectDirection.UP]!!
//        else -> RectDirectionVectors[RectDirection.LEFT]!!
//    }
//
//    if (direction == RectDirection.DOWN) grid.reverse()
//    if (direction == RectDirection.RIGHT) grid = grid.map{it.reversed().toMutableList()}.toMutableList()

    // hell yes man
    // just reverse to fit the direction and un-reverse at the end
    // will work from the top but not from other directions
    for ((y, row) in grid.withIndex()) {
        rockLoop@ for ((x, rock) in row.withIndex()) {
            if (rock != Rock.ROUND) continue@rockLoop
            var oldPos = Vec2(x, y)
            while (true) {
                val newPos = oldPos.add(vec)

                if (newPos.x < 0 || newPos.y < 0 || newPos.y >= grid.size || newPos.x >= row.size || grid[newPos.y][newPos.x] != null) continue@rockLoop

                grid[newPos.y][newPos.x] = rock
                grid[oldPos.y][oldPos.x] = null

                oldPos = newPos
            }
        }
    }

//    if (direction == RectDirection.DOWN) grid.reverse()

    return grid
}

fun calculateNorthLoad(grid: List<List<Rock?>>) = grid.foldIndexed(0) { i, acc, cur ->
    acc + cur.count { it == Rock.ROUND } * (grid.size - i)
}

fun main() {
    val input = getResourceFile("day_14/example.txt").readLines().mapIndexed { y, line ->
        line.toCharArray().mapIndexed { x, item -> CharToRock[item] }
    }

    // Level 1
    var tilted = tilt(input.map { it.toMutableList() }.toMutableList(), RectDirection.UP)

    val checked = mutableSetOf<Pair<Int, RectDirection>>()

    // Level 2
    for (i in 0..<TILT_CYCLES) {
        for (dir in RectDirection.entries) {
            // Detect loop
            // If pattern in this direction has already been checked return result
            val tuple = calculateNorthLoad(tilted) to dir
            if (tuple in checked) {
                val remaining = RectDirection.entries.subList(RectDirection.entries.indexOf(dir)+1, RectDirection.entries.size)

                for (remDir in remaining) {
                    tilted = tilt(tilted, remDir)
                }

                println(calculateNorthLoad(tilted));
                return;
            }
            tilted = tilt(tilted, dir)
            checked += calculateNorthLoad(tilted) to dir
        }
    }

    val northLoad = calculateNorthLoad(tilted)
    println("Load on north: $northLoad")
}