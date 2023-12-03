import java.io.File

fun readFile(fileName: String): List<String> = File(fileName).useLines { it.toList() }

fun <E> List<E>.splitBy(predicate: (E) -> Boolean): List<List<E>> =
    this.fold(mutableListOf(mutableListOf<E>())) { acc, element ->
        if (predicate.invoke(element)) {
            acc += mutableListOf<E>()
        } else {
            acc.last() += element
        }
        acc
    }

fun <T> List<T>.toPair(): Pair<T, T> {
    return this.first() to this.last()
}