package day07

import readInput
import kotlin.math.abs

fun main() {

    fun totalFuel(positions: List<Int>, fuelFor: (Int) -> Int): Int {
        val min = positions.minOrNull() ?: 0
        val max = positions.maxOrNull() ?: 0
        return (min..max)
            .minOfOrNull { position ->
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

    fun solveAMedian(fileName: String): Int {
        val input = readInput(fileName)
        val positions = input.first().split(',').map { it.toInt() }
        fun List<Int>.median() : Int {
            return sorted()[size / 2]
        }
        return positions.median().let { median -> positions.sumOf { abs(it - median) } }
    }

    fun solveB(fileName: String): Int {
        fun arithmeticSequenceSum(size: Int): Int {
            return size * (size + 1) / 2
        }
        return solve(fileName, ::arithmeticSequenceSum)
    }

    check(solveA("day07/Example") == 37)
    check(solveAMedian("day07/Example") == 37)
    check(solveB("day07/Example") == 168)

    val input = "day07/Input.ignore"
    println("Part1: ${solveA(input)}")
    println("Part1 median: ${solveA(input)}")
    println("Part2: ${solveB(input)}")
}