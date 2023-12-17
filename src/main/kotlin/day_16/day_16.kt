package day_16

import RectDirection
import RectDirectionVectors
import Vec2
import getResourceFile
import java.lang.Exception

val MirrorDirections = mapOf(
    '/' to mapOf(
        RectDirection.UP to RectDirection.LEFT,
        RectDirection.RIGHT to RectDirection.DOWN,
        RectDirection.DOWN to RectDirection.RIGHT,
        RectDirection.LEFT to RectDirection.UP
    ),
    '\\' to mapOf(
        RectDirection.UP to RectDirection.RIGHT,
        RectDirection.RIGHT to RectDirection.UP,
        RectDirection.DOWN to RectDirection.LEFT,
        RectDirection.LEFT to RectDirection.DOWN
    )
)

data class LightBeam(var position: Vec2, var direction: RectDirection) {
    fun move() {
        position = position.add(RectDirectionVectors[direction]!!)
    }

    fun mirror(mirror: Char) {
        direction = MirrorDirections[mirror]!![direction]!!
    }

    fun split(splitter: Char) {

    }
}

fun traceBeam(beam: LightBeam, grid: List<CharArray>, energized: MutableSet<Vec2> = mutableSetOf()): Set<Vec2> {
    while (true) {
        beam.move()
        println(beam.position)

        if (beam.position.y < 0 || beam.position.y >= grid.size || beam.position.x < 0 || beam.position.x >= grid[0].size)
            return energized

        val tile = grid[beam.position.y][beam.position.x]

        when (tile) {
            '/', '\\' -> beam.mirror(tile)
            '|', '-' -> throw Exception("Fuc")
//            '|', '-' -> beam.split(tile)
        }
        energized += beam.position
    }
}

fun main() {
    val input = getResourceFile("day_16/example2.txt").readLines().map{it.toCharArray()}

    val light = LightBeam(Vec2(-1, 0), RectDirection.RIGHT)

    val energized = traceBeam(light, input)
    println(energized)
    println(energized.size)
}