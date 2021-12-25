package day25

import readInput

import kotlin.math.max
import kotlin.math.min
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

enum class Cucumber(val char: Char) {
    EAST('>'),
    SOUTH('v')
}

typealias Cucumbers = MutableSet<Coordinate>

data class Coordinate(val x: Int, val y: Int)

data class Map(
    val east: Cucumbers,
    val south: Cucumbers,
    val sizeX: Int,
    val sizeY: Int,
) {
    companion object Factory {
        fun from(lines: List<String>): Map {
            val map = Map(east = mutableSetOf<Coordinate>(),
                south =  mutableSetOf<Coordinate>(),
                sizeX = lines[0].length, sizeY = lines.size)
            lines.forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    Cucumber.values()
                        .firstOrNull { it.char == char }
                        ?.apply { map.add(Coordinate(x,y), this) }
                }
            }
            return map
        }
    }
    fun copyFor(cucumber: Cucumber): Map {
        return when (cucumber) {
            Cucumber.EAST -> copy(east = east.toMutableSet())
            Cucumber.SOUTH ->  copy(south = south.toMutableSet())
        }
    }

    fun all(cucumber: Cucumber) = if (cucumber == Cucumber.EAST) east else south

    fun add(coordinate: Coordinate, cucumber: Cucumber) =
        when (cucumber) {
            Cucumber.EAST -> east.add(coordinate)
            Cucumber.SOUTH -> south.add(coordinate)
        }

    fun remove(coordinate: Coordinate, cucumber: Cucumber) =
        when (cucumber) {
            Cucumber.EAST -> east.remove(coordinate)
            Cucumber.SOUTH -> south.remove(coordinate)
        }

    fun contains(coordinate: Coordinate): Boolean =
        east.contains(coordinate) || south.contains(coordinate)
}

@ExperimentalTime
fun main() {

    fun solve(fileName: String): Int {
        var map = Map.from(readInput(fileName))
        var isCucumbersMoved = true
        var step = 0
        fun move(step: Coordinate, cucumber: Cucumber): Boolean {
            var moved = false
            var stepMap = map.copyFor(cucumber)

            map.all(cucumber).forEach { coordinate ->
                val targetX = (coordinate.x + step.x) % map.sizeX
                val targetY = (coordinate.y + step.y) % map.sizeY
                val targetCoordinate = Coordinate(targetX, targetY)
                if (!map.contains(targetCoordinate)) {
                    stepMap.remove(coordinate, cucumber)
                    stepMap.add(targetCoordinate, cucumber)
                    moved = true
                }
            }
            map = stepMap.copyFor(cucumber)
            return moved
        }

        while (isCucumbersMoved) {
            step+=1
            val eastMoved = move(Coordinate(1, 0), Cucumber.EAST)
            val southMoved = move(Coordinate(0, 1), Cucumber.SOUTH)
            isCucumbersMoved = eastMoved || southMoved
        }
        return step
    }
    check( solve("day25/Example") == 58)

    val input = "day25/Input.ignore"
    val (part1, time1) = measureTimedValue { solve(input) }
    println("Part1: $part1 takes: ${time1.inWholeMilliseconds}ms")
}
