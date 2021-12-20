package day18

import readInput

fun main() {

    fun solveA(fileName: String): Int {
        val numbers = readInput(fileName).map(SnailfishNumber::from)
        return numbers.reduce(SnailfishNumber::plus).magnitude
    }

    fun solveB(fileName: String): Int {
        val input = readInput(fileName)
        return input.indices.flatMap { left -> input.indices.map { right -> left to right } }
            .filter { it.first != it.second }
            .maxOf { (left, right) ->
                (SnailfishNumber.from(input[left]) + SnailfishNumber.from(input[right])).magnitude
            }
    }

    check(solveA("day18/Example") == 4140)
    check(solveB("day18/Example") == 3993)

    val input = "day18/Input.ignore"
    println("Part1: ${solveA(input)}")
    println("Part2: ${solveB(input)}")
}

private class SnailfishNumber(
    var left: SnailfishNumber? = null,
    var right: SnailfishNumber? = null,
    var value: Int? = null,
    var parent: SnailfishNumber? = null,
) {
    companion object Factory {
        fun from(string: String): SnailfishNumber {
            var characters = ArrayDeque(elements = string.toList())
            fun parse(parent: SnailfishNumber): SnailfishNumber {
                val number = SnailfishNumber(parent = parent)
                val character = characters.removeFirst()
                if (character == '[') {
                    number.left = parse(number)
                    characters.removeFirst()
                    number.right = parse(number)
                    characters.removeFirst()
                } else {
                    number.value = character.digitToInt()
                }
                return number
            }

            return parse(SnailfishNumber())
        }
    }

    override fun toString(): String =
        if (left == null) value.toString()
        else "[${left?.toString()},${right?.toString()}]"

    val magnitude: Int
        get() = value ?: (3 * left!!.magnitude + 2 * right!!.magnitude)

    operator fun plus(increment: SnailfishNumber): SnailfishNumber {
        return SnailfishNumber(this, increment).apply {
            left?.parent = this
            right?.parent = this
            reduce()
        }
    }

    fun reduce() {
        numberToExplode?.let { explode(it) }
        numberToSplit?.let { split(it) }
    }

    fun explode(number: SnailfishNumber) {
        val leftValue = number.left?.value
        val rightValue = number.right?.value

        number.left = null
        number.right = null

        number.leftValueNumber?.apply { value = value!! + leftValue!! }
        number.rightValueNumber?.apply { value = value!! + rightValue!! }

        number.value = 0
        reduce()
    }

    fun split(number: SnailfishNumber) {
        val roundedDown = number.value!! / 2
        val roundedUp = (number.value!! + 1) / 2
        number.left = SnailfishNumber(value = roundedDown, parent = number)
        number.right = SnailfishNumber(value = roundedUp, parent = number)
        number.value = null
        reduce()
    }

    val numberToExplode: SnailfishNumber? get() = numberToExplode()
    fun numberToExplode(depth: Int = 0): SnailfishNumber? =
        if (value == null && depth >= 4) this
        else left?.numberToExplode(depth + 1) ?: right?.numberToExplode(depth + 1)

    val numberToSplit: SnailfishNumber? get() =
        if (value ?: 0 >= 10) this
        else left?.numberToSplit ?: right?.numberToSplit

    val leftValueNumber: SnailfishNumber?
        get() = when {
            value != null -> this
            this == parent?.left -> parent?.leftValueNumber
            this == parent?.right -> parent?.left?.rightmostValueNumber
            else -> null
        }

    val rightValueNumber: SnailfishNumber?
        get() = when {
            value != null -> this
            this == parent?.left -> parent?.right?.leftmostValueNumber
            this == parent?.right -> parent?.rightValueNumber
            else -> null
        }

    val leftmostValueNumber: SnailfishNumber?
        get() = if (value != null) this else left?.leftmostValueNumber
    val rightmostValueNumber: SnailfishNumber?
        get() = if (value != null) this else right?.rightmostValueNumber
}
