package day_12

import getResourceFile

enum class Condition {
    BROKEN, OPERATIONAL
}

data class SpringRecord (val conditions: Collection<Condition?>, val pattern: Collection<Int>) {
    companion object {
        fun fromString(string: String): SpringRecord {
            val (conditions, pattern) = string.split(" ")

            return SpringRecord(conditions.toCharArray().map{
                when (it) {
                    '.' -> Condition.OPERATIONAL
                    '#' -> Condition.BROKEN
                    else -> null
                }
            },
                pattern.split(",").map{it.toInt()}
            )
        }
    }

    fun countPossibilities() {
        val groupedConditions = conditions.groupBy{it}.values
        println(groupedConditions)
    }
}

fun main() {
    val records = getResourceFile("day_12/example.txt").readLines().map{SpringRecord.fromString(it)}

    println(records)
    records[1].countPossibilities()
}