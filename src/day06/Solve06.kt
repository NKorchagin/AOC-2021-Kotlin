package day06

import readInput
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main() {
    val ageToDeliver = 6
    val ageToDeliverFirstBaby = 8

    fun handleDay(ages: Map<Int, Long>): Map<Int, Long> {
        val ages = ages.toMutableMap()
        val fishesDelivering = ages[0] ?: 0
        for (age in 0 until ageToDeliverFirstBaby) {
            ages[age] = ages.getOrDefault(age + 1, 0)// shift pregnant by 1 day
        }
        ages[ageToDeliverFirstBaby] = fishesDelivering // new fish babies
        ages[ageToDeliver] = ages[ageToDeliver]?.plus(fishesDelivering) ?: 0 // moms + `ageToDeliver` days of pregnancy
        return ages
    }

    fun amountOfFish(ages: List<Int>, days: Int): Long {
        var agesDict = ages.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        repeat(days) {
            agesDict = handleDay(agesDict)
        }
        return agesDict.values.sum()
    }

    fun solve(fileName: String, days: Int): Long {
        val input = readInput(fileName)
        val fishAges = input.first().split(',').map { it.toInt() }
        return amountOfFish(fishAges, days)
    }

    fun solveA(fileName: String): Long {
        return solve(fileName, 80)
    }

    fun solveB(fileName: String): Long {
        return solve(fileName, 256)
    }

    check(solveA("day06/Example") == 5934L)
    check(solveB("day06/Example") == 26984457539L)

    val input = "day06/Input.ignore"
    val (part1, time1) = measureTimedValue { solveA(input) }
    println("Part1: $part1 takes: ${time1.inWholeMilliseconds}ms")
    val (part2, time2) = measureTimedValue { solveB(input) }
    println("Part2: $part2 takes: ${time2.inWholeMilliseconds}ms")
}