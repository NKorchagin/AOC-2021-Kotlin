package day22

import readInput

import kotlin.math.max
import kotlin.math.min
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main() {

    fun cubesOnApplying(steps: List<Step>): Long {
        var resultMap = mutableMapOf<Cuboid, Int>()
        steps.forEach() { step ->
            var stepMap = mutableMapOf<Cuboid, Int>()
            if (step.isOn) stepMap[step.cuboid] = 1 // enable all for current step
            resultMap.forEach() { (cuboid, count) ->
                // If there is intersection in resultMap with current step decrease to disable and avoid count twice
                val intersection = step.cuboid.intersect(cuboid)
                if (intersection != null) stepMap.decrease(intersection, count)
            }
            resultMap = (resultMap.asSequence() + stepMap.asSequence())
                .distinct()
                .groupBy({ it.key }, { it.value })
                .mapValues { (_, values) -> values.sum() } as MutableMap<Cuboid, Int>
        }
        return resultMap
            .map { it.key.cubesCount * it.value }
            .sum()
    }

    fun solveA(fileName: String): Long {
        val steps = readInput(fileName).map(Step::from)
            .filter { it.isInsideInitializationArea }
        return cubesOnApplying(steps)
    }

    fun solveB(fileName: String): Long {
        val steps = readInput(fileName).map(Step::from)
        return cubesOnApplying(steps)
    }

    check(solveA("day22/Example") == 590784L)
    check(solveA("day22/ExampleB") == 474140L)
    check(solveB("day22/ExampleB") == 2758514936282235L)

    val input = "day22/Input.ignore"
    val (part1, time1) = measureTimedValue { solveA(input) }
    println("Part1: $part1 takes: ${time1.inWholeMilliseconds}ms")
    val (part2, time2) = measureTimedValue { solveB(input) }
    println("Part2: $part2 takes: ${time2.inWholeMilliseconds}ms")
}

fun <T> MutableMap<T, Int>.decrease(key: T, amount: Int = 1): MutableMap<T, Int> {
    this[key] = this.getOrDefault(key, 0) - amount
    return this
}

data class Step(val isOn: Boolean, val cuboid: Cuboid) {
    companion object Factory {
        fun from(string: String): Step = // "on x=-21207..4193,y=-7234..-2286,z=-85723..-60361"
            string.split(' ').let {
                Step(isOn = it[0] == "on", cuboid = Cuboid.from(it[1]))
            }
    }

    val isInsideInitializationArea: Boolean get() = cuboid.all.all { it.first >= -50 && it.last <= 50 }
}

data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {
    companion object Factory {
        fun from(string: String): Cuboid { // "x=-21207..4193,y=-7234..-2286,z=-85723..-60361"
            fun range(string: String): IntRange =
                string.drop(2).split("..")
                    .map { it.toInt() }
                    .let { IntRange(start = it[0], endInclusive = it[1]) }
            return string.split(',').let {
                Cuboid(x = range(it[0]), y = range(it[1]), z = range(it[2]))
            }
        }

        fun fromPairs(pairs: List<Pair<Int, Int>>): Cuboid =
            fromRanges(
                pairs.map { IntRange(start = it.first, endInclusive = it.second) }
            )

        private fun fromRanges(ranges: List<IntRange>): Cuboid = Cuboid(x = ranges[0], y = ranges[1], z = ranges[2])
    }

    val all: List<IntRange> get() = listOf(x, y, z)

    fun intersect(other: Cuboid): Cuboid? {
        val starts = all.zip(other.all) { a, b -> max(a.first, b.first) }
        val ends = all.zip(other.all) { a, b -> min(a.last, b.last) }
        val pairs = (starts zip ends)
        return if (pairs.all { it.first <= it.second }) Cuboid.fromPairs(pairs)
        else null
    }

    val cubesCount: Long get() =
        all.map { (it.last - it.first + 1).toLong() }
            .reduce { a, b -> a * b }
}
