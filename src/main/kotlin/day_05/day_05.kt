package day_05

import java.io.File

enum class Conversions {
    SEED_TO_SOIL, SOIL_TO_FERTILIZER, FERTILIZER_TO_WATER, WATER_TO_LIGHT, LIGHT_TEMPERATURE, TEMPERATURE_TO_HUMIDITY, HUMIDITY_TO_LOCATION
}

data class ConversionMap(val destinationStart: Long, val rangeStart: Long, val rangeLength: Long) {
    companion object {
        fun parse(string: String): ConversionMap {
            val (destination, start, length) = string.split(" ").map { it.toLong() }
            return ConversionMap(destination, start, length)
        }

        fun seedToLocation(seed: Long, maps: List<List<ConversionMap>>) = maps.fold(seed) { value, mapList ->
            val map = mapList.find { it.isInRange(value) }

            return@fold map?.convert(value) ?: value
        }
    }

    fun isInRange(value: Long): Boolean = (value >= rangeStart) && value <= (rangeStart + rangeLength - 1)

    fun convert(value: Long) = destinationStart + value - rangeStart
}

fun main() {
    val input = File("src/main/resources/day_05/input.txt").readLines()

    // Level 1
    // val wantedSeeds = input[0].split(": ")[1].split(" ").map { it.toLong() }

    // Level 2
    // will fill your heap: val wantedSeeds = input[0].split(": ")[1].split(" ").map { it.toLong() }.chunked(2).flatMap {it.first() .. it.first() + it.last() }
    val wantedSeeds = input[0].split(": ")[1].split(" ").map { it.toLong() }.chunked(2)


    val maps = mutableListOf<MutableList<ConversionMap>>()

    for (line in input.subList(2, input.count()).filter { it.trim() != "" }) {
        if (line.endsWith("map:")) {
            maps += mutableListOf<ConversionMap>()
            continue
        }

        val map = ConversionMap.parse(line)
        maps.last() += map
    }

    var min = Long.MAX_VALUE

    // Level 1
    // val seedLocations = wantedSeeds.map { println("yo"); return@map ConversionMap.seedToLocation(it, maps) }

    // Level 2 (takes a little bit to process)
    for (seedRange in wantedSeeds) {
        for (seed in seedRange.first()..seedRange.first() + seedRange.last()) {
            val location = ConversionMap.seedToLocation(seed, maps)
            if (location < min) {
                min = location
            }
        }
    }
    
    println(min)
}