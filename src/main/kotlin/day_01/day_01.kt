package day_01

import java.io.File

fun decodeDigitEntry(entry: String): Int {
    val first = entry.find{ it.isDigit()}
    val last = entry.findLast { it.isDigit() }

    return "$first$last".toInt()
}

val digits = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

val entryRegex = Regex("${digits.keys.joinToString("|")}|[1-9]");

fun stringToDigit(string: String): Int {
    return string.toIntOrNull() ?: digits[string]!!;
}

fun decodeLetterEntry(entry: String): Int {
    val digits = entryRegex.findAll(entry).filter{it.value.trim() != ""}.sortedBy { it.range.first };

    val first = stringToDigit(digits.first().value);
    val last = digits.last();
    var lastInt = stringToDigit(entryRegex.find(entry.substring(last.range.first + 1))?.value ?: last.value);

    return "$first$lastInt".toInt().also{println(it)};
}


fun main() {
    // Level 1
    //val input.txt = File("src/main/resources/day_01/input.txt.txt").readLines()
    //val result = input.txt.fold( 0) { acc, cur -> acc + decodeDigitEntry(cur) }

    // Level 2
    val input = File("src/main/resources/day_01/input.txt.txt").readLines()
    val result = input.filter{it != ""}.fold(0) {acc, cur -> acc + decodeLetterEntry(cur)}

    println("Sum of all calibration values: $result")
}