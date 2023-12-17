package day_13

import getResourceFile

//fun detectMirror(pattern: List<String>): Int? {
//    val rowIdx = pattern.zipWithNext().indexOfFirst { it.first == it.second }.let{if (it >= 0) it else null}
//
//    val columns = mutableListOf<String>()
//
//    for (col in 0..<pattern[0].length) {
//        columns += pattern.map{it[col]}.joinToString("")
//    }
//
//    val colIdx = pattern.zipWithNext().indexOfFirst { it.first == it.second }.let{if (it >= 0) it else null}
//    println(rowIdx)
//    println(colIdx)
//    println()
//    return colIdx ?: rowIdx
//}

fun checkMirror(mirror: Int, pattern: List<String>): Boolean {
    val part1 = pattern.subList(0, mirror).reversed()
    val part2 = pattern.subList(mirror, pattern.size)

    return part1.zip(part2).none{(a, b) -> a != b}
}

fun detectMirror(pattern: List<String>): Int? {
    var first = 0
    var last = 0

    var mirror: Int? = null

    for (i in pattern.indices) {
        val lastIdx = pattern.indexOfLast { it == pattern[i] }
        if (last == 0 && lastIdx >= 0 && lastIdx != i) {
            first = i
            last = lastIdx
        } else if (first+ 1 == last) {
            mirror = last
            break
        } else if (last != 0 && (lastIdx < 0 || lastIdx == i)) {
            break
        } else {
            first = i
            last = lastIdx
        }
    }

    if (mirror == null) return null

    return if (checkMirror(mirror, pattern)) mirror else null
}

fun detectMirrors(pattern: List<String>): Int? {
    val columns = mutableListOf<String>()

    for (col in pattern[0].indices) {
        columns += pattern.map{it[col]}.joinToString("")
    }

    val verticalMirror = detectMirror(columns)
    val horizontalMirror = detectMirror(pattern)

    return verticalMirror ?: horizontalMirror?.let{it*100}
}

fun detectMirrorScuffed(pattern: List<String>): Int? {
    for (i in 1..<pattern.size) {
        if (checkMirror(i, pattern)) return i
    }
    return null
}

fun main() {
    val input = getResourceFile("day_13/example.txt").readLines().joinToString("\n").split("\n\n").map{it.split("\n")}

    val mirrors = input.map{ detectMirrors(it)}

    println("Mirror sum: ${mirrors.sumOf{it ?: 0}}")
}