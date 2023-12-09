package day_07

import getResourceFile

enum class HandType {
    HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_KIND, FULL_HOUSE, FOUR_OF_KIND, FIVE_OF_KIND
}

val cardValues = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')

val jokerCardValues = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

val handTypes = listOf(HandType.THREE_OF_KIND, HandType.FOUR_OF_KIND, HandType.FIVE_OF_KIND)

fun cardToInt(char: Char, joker: Boolean = false): Int =
    (if (joker) jokerCardValues.indexOf(char) else cardValues.indexOf(char))

data class Hand(val cards: List<Int>, val bid: Int) : Comparable<Hand> {
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

        fun getJokerType(cards: List<Int>): HandType {
            val grouped = cards.groupBy { it }.toMutableMap()
            val jokers = grouped.remove(cardToInt('J', true))?.count() ?: 0

            val ofKind = grouped.values.sortedByDescending { it.count() }
            val maxOccurrences = ofKind.getOrNull(0)?.count() ?: return HandType.FIVE_OF_KIND

            if (maxOccurrences + jokers == 5) return HandType.FIVE_OF_KIND
            if (maxOccurrences + jokers == 4) return HandType.FOUR_OF_KIND

            if (jokers == 3 && maxOccurrences == 2 || maxOccurrences + jokers == 3 && ofKind[1].count() == 2 || maxOccurrences == 3 && ofKind[1].count() == 2) return HandType.FULL_HOUSE

            if (jokers == 3 || maxOccurrences + jokers == 3) return HandType.THREE_OF_KIND

            if (maxOccurrences+jokers == 2 && ofKind[1].count() == 2 || maxOccurrences == 2 && ofKind[1].count() + jokers == 2 || maxOccurrences == 2 && ofKind[1].count() == 2) return HandType.TWO_PAIR

            if (maxOccurrences == 2 || jokers == 2 || maxOccurrences + jokers == 2) return HandType.ONE_PAIR

            return HandType.HIGH_CARD
        }
    }
//    Level 1
//    val type = Companion.getType(cards)

    // Level 2
    val type = Companion.getJokerType(cards)

    override fun compareTo(other: Hand): Int {
        val typeCompare = type.compareTo(other.type)
        if (typeCompare != 0) return typeCompare

        for ((a, b) in cards.zip(other.cards)) {
            if (a != b) return a.compareTo(b)
        }

        return 0
    }
}

fun main() {
    val input = getResourceFile("day_07/input.txt").readLines()

    val hands = input.map { hand ->
        val (cards, bid) = hand.split(" ")
        return@map Hand(cards.toCharArray().map { cardToInt(it, true) }, bid.toInt())
    }

    val sortedHands = hands.sorted()

    println(sortedHands)
    val totalWinnings = sortedHands.foldIndexed(0) { index, acc, hand ->
        (index + 1) * hand.bid + acc
    }

    println("Total winnings: $totalWinnings")
}