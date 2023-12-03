package day_03

import java.io.File

enum class Direction { UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT }

val DirectionVectors = hashMapOf(
    Direction.UP to Vec2(0, -1),
    Direction.UP_RIGHT to Vec2(1, -1),
    Direction.RIGHT to Vec2(1, 0),
    Direction.DOWN_RIGHT to Vec2(1, 1),
    Direction.DOWN to Vec2(0, 1),
    Direction.DOWN_LEFT to Vec2(-1, 1),
    Direction.LEFT to Vec2(-1, 0),
    Direction.UP_LEFT to Vec2(-1, -1)
)

data class Vec2(val x: Int, val y: Int) {
    companion object {
        fun equals(a: Vec2, b: Vec2) = a.x == b.x && a.y == b.y
        fun add(a: Vec2, b: Vec2) = Vec2(a.x + b.x, a.y + b.y)

        fun getNeighbors(vec: Vec2) = DirectionVectors.values.map { vec.add(it) }

        fun getSafeNeighbors(vec: Vec2) = getNeighbors(vec).filter { it.x >= 0 && it.y >= 0 }

        fun getLineNeighbors(a: Vec2, b: Vec2): List<Vec2> {
            if (a == b) return a.getSafeNeighbors()

            val neighbors = mutableListOf<Vec2>()

            for (x in a.x..b.x) {
                val vec = Vec2(x, a.y)
                neighbors += vec.getSafeNeighbors()
            }

            return neighbors.distinct()
                .filter { it != a && it != b && (if (it.y != a.y) true else it.x < a.x || it.x > b.x) }
        }
    }

    override fun equals(other: Any?): Boolean = (other is Vec2) && Companion.equals(this, other)

    fun add(vec: Vec2) = Companion.add(this, vec)

    fun getNeighbors() = Companion.getNeighbors(this)

    fun getSafeNeighbors() = Companion.getSafeNeighbors(this)
}

data class SchematicPart(val start: Vec2, val end: Vec2, val value: Int)

data class Gear(val parts: List<SchematicPart>, val position: Vec2) {
    fun getRatio() = parts.fold(1) { ratio, part -> ratio * part.value }
}

data class Schematic(val parts: List<SchematicPart>, val symbols: List<Vec2>, val gearCandidates: List<Vec2>) {
    fun findPartNumbers() = parts.mapNotNull { part ->
        val neighbors = Vec2.getLineNeighbors(part.start, part.end)
        if (neighbors.any { it in symbols }) {
            return@mapNotNull part.value
        }
        return@mapNotNull null
    }

    fun findGears() = gearCandidates.mapNotNull { gear ->
        val gearParts = parts.filter { gear in Vec2.getLineNeighbors(it.start, it.end) }

        if (gearParts.count() == 2) return@mapNotNull Gear(gearParts, gear)

        return@mapNotNull null
    }
}

fun parseSchematic(input: List<String>): Schematic {
    val parts = mutableListOf<SchematicPart>()
    val symbols = mutableListOf<Vec2>()
    val gearCandidates = mutableListOf<Vec2>()

    var currentNum = ""
    var currentStart: Vec2? = null

    input.forEachIndexed { y, line ->
        if (currentStart != null) {
            parts += SchematicPart(currentStart!!, Vec2(line.length - 1, currentStart!!.y), currentNum.toInt())
        }

        currentNum = ""
        currentStart = null

        line.forEachIndexed { x, it ->
            if (it.isDigit()) {
                if (currentNum == "") {
                    currentStart = Vec2(x, y)
                }
                currentNum += it
            } else {
                if (currentStart != null) {
                    parts += SchematicPart(currentStart!!, Vec2(x - 1, y), currentNum.toInt())
                    currentNum = ""
                    currentStart = null
                }

                if (it != '.') {
                    symbols += Vec2(x, y)
                }

                if (it == '*') {
                    gearCandidates += Vec2(x, y)
                }
            }
        }
    }

    return Schematic(parts, symbols, gearCandidates)
}

fun main() {
    val input = File("src/main/resources/day_03/input.txt").readLines()

    val schematic = parseSchematic(input)

    // Level 1
//    val partNumbers = schematic.findPartNumbers()
//
//    println("Parts: ${schematic.parts.count()}")
//    println(schematic.parts)
//
//    println("Symbols: ${schematic.symbols.count()}")
//    println(schematic.symbols)
//
//    println("Sum of part numbers: ${partNumbers.sum()}")

    val gears = schematic.findGears()
    println("Gears: ${gears.count()}")
    println(gears)
    println("Ratio: ${gears.sumOf { it.getRatio() }}")
}