import Result.*

fun main() {

    val (rulesRaw, partsRaw) = readFile("input/19.txt").splitBy { it.isEmpty() }

    val rules = rulesRaw.map {
        val (ruleName, checksRaw) = it.split("{")
        val checks = checksRaw.replace("}", "").split(",")
        Rule(ruleName, checks.map { checkString -> Check(checkString) })
    }

    val parts = partsRaw.map {
        val (x, m, a, s) = it.substring(1, it.length - 1).split(",")
        Part(x.extractInt(), m.extractInt(), a.extractInt(), s.extractInt())
    }

    parts.forEach { part ->
        var rule: Rule? = rules.find { rule -> rule.name == "in" }

        while (rule != null) {
            when (val result = rule.evaluate(part)) {
                is Result -> {
                    part.result = result
                    rule = null
                }

                is String -> rule = rules.find { nextRule -> nextRule.name == result }
            }
        }
    }

    val sum = parts
        .filter { it.result == Accepted }
        .sumOf { it.sum() }

    println(sum)
}

data class Rule(val name: String, val checks: List<Check>) {
    fun evaluate(part: Part): Any {
        for (check in checks) {
            if (!check.check.contains(":")) {
                //next rule to be evaluated
                return when (check.check) {
                    "A" -> Accepted
                    "R" -> Rejected
                    else -> check.check
                }
            } else {
                val (equation, result) = check.check.split(":")

                val successful = equation.solve(part)

                if (successful) {
                    println("result: $result")
                    return when (result) {
                        "A" -> Accepted
                        "R" -> Rejected
                        else -> result
                    }
                } else {
                    continue
                }
            }
        }
        return Unknown
    }
}

private fun String.solve(part: Part): Boolean {
    val variable = this[0].toString()
    val operator = this[1]
    val value = this.substring(2).toInt()

    return when (operator) {
        '<' -> part.getValue(variable) < value
        '>' -> part.getValue(variable) > value
        else -> error("unknown operator")
    }
}

data class Check(val check: String)

enum class Result {
    Accepted, Rejected, Unknown
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int, var result: Result = Unknown) {
    fun getValue(name: String): Int {
        return when (name) {
            "x" -> x
            "m" -> m
            "a" -> a
            "s" -> s
            else -> error("man i dont know")
        }
    }

    fun sum() = x + m + a + s
}

private fun String.extractInt() = this.split("=").last().toInt()
