fun main() {
    val lines = readFile("input/8.txt")

    val instructions = lines[0]
    val nodes = lines
        .drop(2)
        .associate {
            val (node, directions) = it.split(" = ")
            val (left, right) = directions.replace("(", "").replace(")", "").split(", ")
            node to Node(left, right)
        }

    // pt1
    var currentNode = "AAA"
    for (steps in 0..Int.MAX_VALUE) {
        val nextDirection = instructions[steps % instructions.length]
        val nextNode = nodes[currentNode]!!
        currentNode = when (nextDirection) {
            'L' -> nextNode.left
            'R' -> nextNode.right
            else -> error("wrong direction")
        }

        if (currentNode == "ZZZ") {
            println(steps + 1)
            break
        }
    }

    // pt2
    var currentNodes = nodes.filter { it.key.endsWith("A") }.keys.toList()

    for (steps in 0..Int.MAX_VALUE) {
        val nextDirection = instructions[steps % instructions.length]

        currentNodes = currentNodes.map { node ->
            val nextNode = nodes[node]!!
            when (nextDirection) {
                'L' -> nextNode.left
                'R' -> nextNode.right
                else -> error("wrong direction")
            }
        }

        // no this doesnt work
        if (currentNodes.all { it.endsWith("Z") }) {
            println(steps + 1)
            break
        }
    }
}

data class Node(val left: String, val right: String)