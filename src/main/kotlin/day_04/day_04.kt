package day_04

import java.io.File
import kotlin.math.pow

data class Scratchcard(val id: Int, val winnerSet: HashSet<Int>, val values: List<Int>) {
    companion object {
        fun fromString(string: String): Scratchcard {
            val (cardName, valueString) = string.split(": ")

            val id = cardName.split("\\s+".toRegex())[1].toInt()

            val (winners, values) = valueString.split(" | ").map { it.trim().split("\\s+".toRegex()) }

            return Scratchcard(id, winners.map { it.toInt() }.toHashSet(), values.map { it.toInt() })

        }

        fun calculateCopies(cards: List<Scratchcard>): MutableList<Int> {
            val copies = cards.map { 1 }.toMutableList()

            cards.forEach { card ->
                val winnings = card.findWinners().count()

                if (winnings == 0) return@forEach

                for (i in (card.id)..<(card.id + card.findWinners().count())) {
                    copies[i] += copies[card.id - 1]
                }
            }

            return copies;
        }
    }

    fun findWinners() = values.filter { it in winnerSet }

    fun calculatePoints(): Int {
        val winners = findWinners()

        if (winners.isEmpty()) return 0

        return (2.0).pow(winners.count() - 1).toInt()
    }
}

fun main() {
    val input = File("src/main/resources/day_04/input.txt").readLines()

    val cards = input.map { Scratchcard.fromString(it) }

    val points = cards.map { it.calculatePoints() }

    // Level 1
    println(points)
    println("Sum of points ${points.sum()}")

    // Level 2
    val copies = Scratchcard.calculateCopies(cards)
    println(copies)
    println("Sum of copies: ${copies.sum()}")
}