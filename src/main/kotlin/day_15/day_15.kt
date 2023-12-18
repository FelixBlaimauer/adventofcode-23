package day_15

import getResourceFile

fun holidayHasher(string: String) = string.toCharArray().fold(0) { cur, it -> ((cur + it.code) * 17) % 256 }

data class Lens(val label: String, val focalLength: Int)

data class Box(val id: Int, val lenses: MutableList<Lens> = mutableListOf()) {}

fun handleInitStep(box: Box, lens: Lens, operation: Char) {
    when (operation) {
        '-' -> box.lenses.remove(lens)
        '=' -> println()
    }
}

fun main() {
    val steps = getResourceFile("day_15/input.txt").readText().replace("[\n\r]".toRegex(), "").split(",")

    // Level 1
//    val hashes = steps.map{ holidayHasher(it)}
//
//    println(hashes.sum())

    // Level 2

    val boxes = Array(256) { Box(it) }

    for (step in steps) {
        val operation = step.find { it == '-' || it == '=' }!!

        val (label, lensStr) = step.split("[-=]".toRegex())

        val box = boxes[holidayHasher(label)]
        val focalLength = lensStr.toIntOrNull()

        when (operation) {
            '-' -> box.lenses.removeAll { it.label == label }
            '=' -> if (box.lenses.indexOfFirst { it.label == label } < 0) {
                box.lenses += Lens(label, focalLength!!)
            } else {
                box.lenses.replaceAll { if (it.label == label) Lens(label, focalLength!!) else it }
            }
        }
    }

    val powers = boxes.mapIndexed {boxIdx, it ->
        it.lenses.foldIndexed(0) {i, acc, cur -> acc + cur.focalLength * (i+1) * (boxIdx+1)}
    }

    println(powers)
    println(powers.sum())
}