fun main() {
    val ageToDeliver = 6
    val ageToDeliverFirstBaby = 8

    fun handleDay(ages: Map<Int, Long>): Map<Int, Long> {
        var ages = ages.toMutableMap()
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

    fun part1(input: String): Long {
        return solve(input, 80)
    }

    fun part2(input: String): Long {
        return solve(input, 256)
    }

    check(part1("Day06/Day06_test") == 5934L)
    check(part2("Day06/Day06_test") == 26984457539L)

    val input = "Day06/Day06.ignore"
    println("Part1: ${part1(input)}")
    println("Part1: ${part2(input)}")
}