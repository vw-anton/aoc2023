val example1 = listOf(
    7 to 9,
    15 to 40,
    30 to 200
)

val part1 = listOf(
    44 to 283,
    70 to 1134,
    70 to 1134,
    80 to 1491
)

val example2 = listOf(
    71530 to 940200
)

val part2 = listOf(
    44707080 to 283113411341491
)

fun main() {
    val result = part2.map {
        val tries = List(it.first + 1) { buttonDuration ->
            (it.first - buttonDuration).toLong() * buttonDuration.toLong()
        }.filter { duration -> duration > it.second }
        tries.size
    }
        .also { println(it) }
        .reduce { acc, i -> acc * i }

    println(result)
}