package day_03

import Vec2
import java.io.File

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
    val input = File("src/main/resources/day_03/hofer.txt").readLines()

    val schematic = parseSchematic(input)

    // Level 1
    val partNumbers = schematic.findPartNumbers()

    println("Parts: ${schematic.parts.count()}")
//    println(schematic.parts)

    println("Symbols: ${schematic.symbols.count()}")
//    println(schematic.symbols)

    println("Part count ${partNumbers.count()}")
    println("Sum of part numbers: ${partNumbers.sum()}")

    val gears = schematic.findGears()
    println("Gears: ${gears.count()}")
//    println(gears)
    println("Ratio: ${gears.sumOf { it.getRatio() }}")
}