package day_11

import Vec2
import getResourceFile

const val EXPANSION_FACTOR = 10

fun expandUniverse(galaxy: List<CharArray>): List<List<Char>> {
    val expanded = mutableListOf<MutableList<Char>>()
    for (row in galaxy) {
        if (row.all { it == '.' })
            expanded += row.toMutableList()

        expanded += row.toMutableList()
    }

    var colsExpanded = 0

    for (x in 0..<expanded[0].count()) {
        val column = expanded.map { it[x + colsExpanded] }

        if (column.all { it == '.' }) {
            expanded.forEach { row -> row.add(index = x + colsExpanded, row[x + colsExpanded]) }
            colsExpanded++
        }
    }

    return expanded
}

fun markExpansions(galaxy: List<CharArray>): List<List<Char>> {
    val marked = mutableListOf<MutableList<Char>>()

    for (row in galaxy) {
        marked += if (row.all { it == '.' })
            row.map { 'M' }.toMutableList()
        else
            row.toMutableList()
    }


    for (x in 0..<marked[0].count()) {
        val column = marked.map { it[x] }

        if (column.all { it == '.' || it == 'M' }) {
            marked.forEach { row -> row[x] = 'M' }
        }
    }

    return  marked
}

fun main() {
    // Level 1
//    val universe = expandUniverse(getResourceFile("day_11/example.txt").readLines().map { it.toCharArray() })
    val universe = markExpansions(getResourceFile("day_11/example.txt").readLines().map { it.toCharArray() })

    val galaxies = mutableListOf<Vec2>()

    for ((y, row) in universe.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == '#') galaxies += Vec2(x, y)
        }
    }

    var distance = 0

    for (i in 0..<galaxies.count()) {
        for (j in i..<galaxies.count()) {
            val yOccurrences = universe.subList(galaxies[i].y, galaxies[j].y).count{it[0] == 'M'}
            val xOccurrences = (if (galaxies[j].x < galaxies[i].x ) universe[0].subList(galaxies[j].x, galaxies[i].x) else universe[0].subList(galaxies[i].x, galaxies[j].x)).count{it == 'M'}
            val occurrences = xOccurrences + yOccurrences
            distance += Vec2.manhattanDistance(galaxies[i], galaxies[j]) - occurrences + EXPANSION_FACTOR * occurrences
        }
    }

    println(distance)
}