package day_02

import java.io.File
import kotlin.math.min

const val MAX_RED = 12
const val MAX_GREEN = 13;
const val MAX_BLUE = 14;

interface GameFactory {
    fun fromString(string: String): Game
}

class HelperElf {
    companion object : GameFactory {
        override fun fromString(string: String): Game {

            val (gameTitle, revealList) = string.split(":").map { it.trim() };

            val gameId = gameTitle.split(" ")[1].toInt();

            val reveals = revealList.split(";").map { revealSet ->
                val colorReveals = revealSet.split(",").map { it.trim() };

                val red = colorReveals.find { it.contains("red") }?.split(" ")?.first()?.toInt();
                val green = colorReveals.find { it.contains("green") }?.split(" ")?.first()?.toInt();
                val blue = colorReveals.find { it.contains("blue") }?.split(" ")?.first()?.toInt();


                return@map Reveal(red, green, blue);
            }

            return Game(gameId, reveals);
        }
    }
}

data class RGBValue(var red: Int, var green: Int, var blue: Int)

data class Reveal(var red: Int?, var green: Int?, var blue: Int?) {
    fun isImpossible(): Boolean = (red ?: 0) > MAX_RED || (green ?: 0) > MAX_GREEN || (blue ?: 0) > MAX_BLUE;
}

data class Game(val id: Int, val reveals: List<Reveal>) {
    fun isImpossible(): Boolean = reveals.any { it.isImpossible() }

    fun calculateMinimum() = reveals.reduce { min, cur ->
        if ((cur.red ?: 0) > (min.red ?: 0)) {
            min.red = cur.red
        }
        if ((cur.green ?: 0) > (min.green ?: 0)) {
            min.green = cur.green
        }
        if ((cur.blue ?: 0) > (min.blue ?: 0)) {
            min.blue = cur.blue
        }
        return@reduce min
    }

    fun calculateMinimumPower(): Int {
        val minimum = calculateMinimum();

        return (minimum.red ?: 1) * (minimum.green ?: 1) * (minimum.blue ?: 1);
    }
}

fun main() {
    val input = File("src/main/resources/day_02/input.txt").readLines().map { it.trim() }.filter { it != "" };

    val games = input.map { HelperElf.fromString(it) }

//    Level 1
//    val possibleGames = games.filter { !it.isImpossible() }
//
//    println(possibleGames);
//    println("Sum of ids: ${possibleGames.sumOf{it.id}}")

    val minimalPowers = games.map { it.calculateMinimumPower() }
    print("Sum of minimal powers: ${minimalPowers.sum() }")
}