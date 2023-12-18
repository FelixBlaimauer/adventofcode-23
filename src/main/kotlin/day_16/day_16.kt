package day_16

import OpposingRectDirection
import RectDirection
import RectDirectionVectors
import Vec2
import getResourceFile

val MirrorDirections = mapOf(
    '/' to mapOf(
        RectDirection.UP to RectDirection.LEFT,
        RectDirection.RIGHT to RectDirection.DOWN,
        RectDirection.DOWN to RectDirection.RIGHT,
        RectDirection.LEFT to RectDirection.UP
    ), '\\' to mapOf(
        RectDirection.UP to RectDirection.RIGHT,
        RectDirection.RIGHT to RectDirection.UP,
        RectDirection.DOWN to RectDirection.LEFT,
        RectDirection.LEFT to RectDirection.DOWN
    )
)

val SplitterDirections = mapOf(
    '-' to listOf(RectDirection.LEFT, RectDirection.RIGHT), '|' to listOf(RectDirection.UP, RectDirection.DOWN)
)

data class LightBeam(var position: Vec2, var direction: RectDirection) {
    fun move() {
        position = position.add(RectDirectionVectors[direction]!!)
    }

    fun mirror(mirror: Char) {
        direction = MirrorDirections[mirror]!![OpposingRectDirection[direction]]!!
    }

    fun split(splitter: Char, grid: List<CharArray>, visited: MutableSet<LightBeam>): Set<Vec2>? {
        if (direction in SplitterDirections[splitter]!!) return null

        return SplitterDirections[splitter]!!.map { traceBeam(LightBeam(position, it), grid, visited) }
            .reduce { a, b -> a + b }
    }
}

fun traceBeam(
    beam: LightBeam,
    grid: List<CharArray>,
    visited: MutableSet<LightBeam> = mutableSetOf(),
    energized: MutableSet<Vec2> = mutableSetOf()
): Set<Vec2> {
    while (true) {
        beam.move()
        if (beam in visited || beam.position.y < 0 || beam.position.y >= grid.size || beam.position.x < 0 || beam.position.x >= grid[0].size) return energized

        energized += beam.position
        visited += beam

        val tile = grid[beam.position.y][beam.position.x]

        when (tile) {
            '/', '\\' -> beam.mirror(tile)
            '|', '-' -> {
                val traced = beam.split(tile, grid, visited)
                if (traced != null) return energized + traced
            }
        }
    }
}

fun main() {
    val input = getResourceFile("day_16/input.txt").readLines().map { it.toCharArray() }
    // Level 1
//    val light = LightBeam(Vec2(-1, 0), RectDirection.RIGHT)

//    val energized = traceBeam(light, input)

    // Level 2
    val energized = mutableListOf<Int>()
    for (y in input.indices) {
        val beams =
            listOf(LightBeam(Vec2(-1, y), RectDirection.RIGHT), LightBeam(Vec2(input[0].size, y), RectDirection.LEFT))

        beams.forEach {
            energized += traceBeam(it, input).size
        }
    }

    for (x in input[0].indices) {
        val beams = listOf(
            LightBeam(Vec2(x, -1), RectDirection.DOWN), LightBeam(Vec2(x, input.size), RectDirection.UP)
        )

        beams.forEach {
            energized += traceBeam(it, input).size
        }
    }


//    println(energized)
    println(energized.max())
}