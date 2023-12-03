fun main() {

    val map = mapOf(
        "one" to "1e",
        "two" to "2o",
        "three" to "3e",
        "four" to "4r",
        "five" to "5e",
        "six" to "6x",
        "seven" to "7n",
        "eight" to "8t",
        "nine" to "9e",
    )

    val result = readFile("input/1.txt")
        .map {
            var indexToMatch: Pair<Int, String>? = 0 to ""
            var replaced = it
            var index = 0
            while (indexToMatch != null) {
                indexToMatch = it.findAnyOf(map.keys, index)

                if (indexToMatch != null) {
                    replaced =
                        replaced.replaceFirst(indexToMatch.second, map.getOrDefault(indexToMatch.second, "ERROR"))
                    index = indexToMatch.first+1
                }
            }

            replaced
        }
        .map { line ->
            val digits = line.toCharArray().filter { it.isDigit() }
            "${digits.first()}${digits.last()}"
        }.sumOf {
            it.toInt()
        }

    print(result)

}