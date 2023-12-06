package day_06

import getResourceFile

data class Race(val time: Long, val distance: Long) {
    fun isBeatenBy(chargeTime: Long) = chargeTime * (time - chargeTime) > distance

    fun getPossibilities(): Int {
        var winCount = 0

        for (i in 1..<time) {
            if (isBeatenBy(i)) winCount++
        }

        return winCount
    }
}

fun main() {
    val input = getResourceFile("day_06/input.txt").readLines()

    // Level 1
//    val times = input[0].split(":\\s+".toRegex())[1].split("\\s+".toRegex()).map { it.toInt() }
//    val distances = input[1].split(":\\s+".toRegex())[1].split("\\s+".toRegex()).map { it.toInt() }
//
//    val races = times.zip(distances).map { (time, distance) -> Race(time, distance) }
//
//    val wins = races.map { it.getPossibilities() }
//
//    println(wins)
//    println("Multiplied wins: ${
//        wins.reduce { acc, cur -> acc * cur }
//    }")

    // Level 2
    val time = input[0].split(":\\s+".toRegex())[1].replace(" ", "").toLong()
    val distance = input[1].split(":\\s+".toRegex())[1].replace(" ", "").toLong()
    val race = Race(time, distance)

    println("Possibilities: ${race.getPossibilities()}")
}