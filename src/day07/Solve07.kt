package day07

import readInput
import kotlin.math.abs

fun main() {

    fun totalFuel(positions: List<Int>, fuelFor: (Int) -> Int): Int {
        val min = positions.minOrNull() ?: 0
        val max = positions.maxOrNull() ?: 0
        return (min..max).minOfOrNull { position ->
            positions.sumOf { fuelFor(abs(position - it)) }
        } ?: 0
    }

    fun solve(fileName: String, fuelFor: (Int) -> Int): Int {
        val input = readInput(fileName)
        val positions = input.first().split(',').map { it.toInt() }
        return totalFuel(positions, fuelFor)
    }

    fun solveA(fileName: String): Int {
        return solve(fileName) { it }
    }

    fun solveB(fileName: String): Int {
        return solve(fileName) { it * (it + 1) / 2 }
    }

    check(solveA("day07/Example") == 37)
    check(solveB("day07/Example") == 168)

    val input = "day07/Input.ignore"
    println("Part1: ${solveA(input)}")
    println("Part2: ${solveB(input)}")
}