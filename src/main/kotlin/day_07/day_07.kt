package day_07

import getResourceFile

enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_KIND,
    FULL_HOUSE,
    FOUR_OF_KIND,
    FIVE_OF_KIND
}

val cardValues = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')

val handTypes = listOf(HandType.THREE_OF_KIND, HandType.FOUR_OF_KIND, HandType.FIVE_OF_KIND)

fun cardToInt(char: Char): Int = cardValues.indexOf(char) + 2

data class Hand(val cards: List<Int>, val bid: Int) {
    companion object {
        fun getType(cards: List<Int>): HandType {
            val grouped = cards.groupBy { it }
            val ofKind = grouped.values.sortedByDescending { it.count() }

            val maxOccurrences = ofKind[0].count()

            if (maxOccurrences == 3 && ofKind[1].count() == 2) return HandType.FULL_HOUSE
            if (maxOccurrences == 2) return if (ofKind[1].count() == 2) HandType.TWO_PAIR else HandType.ONE_PAIR
            if (maxOccurrences == 1) return HandType.HIGH_CARD

            return handTypes[maxOccurrences - 3]
        }
    }

    val type = Companion.getType(cards)
}

fun main() {
    val input = getResourceFile("day_07/example.txt").readLines()

    val hands = input.map { hand ->
        val (cards, bid) = hand.split(" ")
        return@map Hand(cards.toCharArray().map { cardToInt(it) }, bid.toInt())
    }

    // println(hands.sortedWith(compareBy({it.type}, {})))
}