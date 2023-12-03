fun main() {
    val games = readFile("input/2.txt")
        .mapIndexed { idx, line ->
            val sets = line.split(":").last().split(";").map {
                val cubeSet = it.trim().split(",")
                var red = 0
                var blue = 0
                var green = 0
                cubeSet.map { countToCube ->
                    val split = countToCube.trim().split(" ")
                    when (split.last()) {
                        "red" -> red = split.first().toInt()
                        "blue" -> blue = split.first().toInt()
                        "green" -> green = split.first().toInt()
                    }
                }

                CubeSet(red = red, blue = blue, green = green)
            }

            Game(idx + 1, sets)
        }

    println(games)

    val configuration = CubeSet(red = 12, green = 13, blue = 14)

    // pt 1
    val result = games
        .filter { game ->
            game.sets.all { it.canBePlayed(configuration) }
        }
        .onEach { println(it) }
        .sumOf { game -> game.id }

    println(result)

    // pt 2
    val reduced = games.map { game ->
        game.sets.reduce { acc, current ->
            CubeSet(
                red = maxOf(acc.red, current.red),
                green = maxOf(acc.green, current.green),
                blue = maxOf(acc.blue, current.blue)
            )
        }
    }.sumOf { it.green * it.red * it.blue }


    println(reduced)
}

data class Game(val id: Int, val sets: List<CubeSet>)

data class CubeSet(
    val red: Int,
    val blue: Int,
    val green: Int
)

fun CubeSet.canBePlayed(configuration: CubeSet): Boolean {
    return this.red <= configuration.red
            && this.blue <= configuration.blue
            && this.green <= configuration.green
}

