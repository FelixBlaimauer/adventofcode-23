package day_09

import getResourceFile

fun predictMeasurement(values: List<Int>): List<Int> {
    if (values.all { it == 0 }) return values

    val seq = predictMeasurement(values.zipWithNext().map { (first, second) -> second - first })
    return seq + listOf(values.last() + seq.last())
}

fun main() {
    val input = getResourceFile("day_09/input.txt").readLines().map { line -> line.split(" ").map { it.toInt() } }

    // Level 1
//    val predictedValues = input.map { predictMeasurement(it).last() }

    val predictedValues = input.map{ predictMeasurement(it.reversed()).last()}

    println("Sum of predictions: ${predictedValues.sum()}")
}