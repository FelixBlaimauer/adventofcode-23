package day_05

import java.io.File

enum class Conversions {
    SEED_TO_SOIL, SOIL_TO_FERTILIZER, FERTILIZER_TO_WATER, WATER_TO_LIGHT, LIGHT_TEMPERATURE, TEMPERATURE_TO_HUMIDITY, HUMIDITY_TO_LOCATION
}

data class ConversionMap(val destinationStart: Int, val rangeStart: Int, val rangeLength: Int) {
    companion object {
        fun parse(string: String): ConversionMap {
            val (destination, start, length) = string.split(" ").map { it.toInt() }
            return ConversionMap(destination, start, length)
        }

        fun seedToLocation(seed: Int, maps: List<List<ConversionMap>>) = maps.fold(seed) { value, mapList ->
            val map = mapList.find { it.isInRange(value) }

            return@fold map?.convert(value) ?: value
        }
    }

    fun isInRange(value: Int): Boolean = (value >= rangeStart) && value <= (rangeStart + rangeLength - 1)

    fun convert(value: Int) = destinationStart + value - rangeStart
}

fun main() {
    val input = File("src/main/resources/day_05/input.txt").readLines()

    val wantedSeeds = input[0].split(": ")[1].split(" ").map { it.toInt() }

    val maps = mutableListOf<MutableList<ConversionMap>>()

    for (line in input.subList(2, input.count()).filter { it.trim() != "" }) {
        if (line.endsWith("map:")) {
            maps += mutableListOf<ConversionMap>()
            continue
        }

        val map = ConversionMap.parse(line)
        maps.last() += map
    }

    val seedLocations = wantedSeeds.map { ConversionMap.seedToLocation(it, maps) }

    println(seedLocations)
    println(seedLocations.min())
}