val cardValues = "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2".split(", ").reversed()
val cardValuesPt2 = "A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J".split(", ").reversed()

fun main() {
    val hands = readFile("input/7.txt").map {
        Hand.fromString(it)
    }

    val result = hands
        .sorted()
        .also { println(it) }
        .mapIndexed { index, hand ->
            (index + 1) * hand.bid
        }
        .sum()

    println(result)
}

data class Hand(val cards: List<String>, val bid: Int) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int = when {
        this.handValue() > other.handValue() -> 1
        this.handValue() < other.handValue() -> -1
        else -> this.cards.compareSingleValues(other.cards)
    }

    override fun toString(): String {
        return "\n$cards: ${handValue()} - $bid"
    }

    private fun handValue(): Int {
        return cards
            .groupingBy { it.first() }
            .eachCount()
            .toMutableMap()
            .type()
    }

    companion object {
        fun fromString(input: String): Hand {
            return with(input.split(" ")) {
                Hand(this.first().chunked(1), this.last().toInt())
            }
        }
    }

}

private fun MutableMap<Char, Int>.type(): Int {
    val jokerCount = this.remove('J') ?: 0

    val type = when {
        this.containsValue(5) -> 500 // five of a kind
        this.containsValue(4) -> 400 // four of a kind
        this.containsValue(3) && this.containsValue(2) -> 350 // full house
        this.containsValue(3) -> 300 // three of a kind
        this.count { it.value == 2 } == 2 -> 250 // two pair
        this.count { it.value == 2 } == 1 -> 200 // one pair
        else -> 100 // high card
    }

    return jokerScore(type, jokerCount)
}

private fun jokerScore(type: Int, jokerCount: Int): Int {
    if (jokerCount == 0)
        return type

    if (type == 250 || type == 350)
        return 350

    return when (jokerCount) {
        0 -> type
        5 -> 500
        else -> type + 100 * jokerCount
    }
}

private fun List<String>.compareSingleValues(cards: List<String>): Int {
    this.forEachIndexed { idx, card ->
        val result = when {
            card.cardValue() > cards[idx].cardValue() -> 1
            card.cardValue() < cards[idx].cardValue() -> -1
            else -> 0
        }
        if (result != 0) {
            return result
        }
    }
    return 0
}

private fun String.cardValue() = cardValuesPt2.indexOf(this)

