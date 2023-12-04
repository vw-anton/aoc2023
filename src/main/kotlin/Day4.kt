import kotlin.math.pow

fun main() {
    val lines = readFile("input/4.txt")

    val cards = lines
        .map {
            it.split(": ").last().split(" | ").toPair()
        }
        .map {
            it.first.trim().split(" ") to it.second.trim().split(" ")
        }.map {
            ScratchCard(
                winningNumbers =
                it.first.filter { s -> s.isNotBlank() }.map { n1 -> n1.toInt() },
                numbers = it.second.filter { s -> s.isNotBlank() }.map { n2 ->
                    n2.toInt()
                })
        }

    // pt1
    println(cards.toMutableList().map { scratchCard ->
        scratchCard.matchCount()
    }.sumOf { matchCount ->
        when {
            matchCount == 1 -> 1.0
            matchCount > 1 -> 2.0.pow(matchCount - 1)
            else -> 0.0
        }
    }.toInt())

    // pt2
    var hasCardsToPlay = true

    while (hasCardsToPlay) {
        cards.forEachIndexed { idx, card ->
            if (card.open > 0) {
                card.open -= 1

                val matchCount = card.matchCount()
                if (matchCount > 0) {
                    for (i in 1..matchCount) {
                        cards[idx + i].open += 1
                    }
                }
            }
        }

        hasCardsToPlay = cards.any { it.open > 0 }
    }

    println(cards.sumOf { it.played })

}

data class ScratchCard(
    val winningNumbers: List<Int>,
    val numbers: List<Int>,
    var open: Int = 1,
    var played: Int = 0
) {
    fun matchCount(): Int {
        played += 1
        return numbers.intersect(winningNumbers.toSet()).size
    }

}