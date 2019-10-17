package eu.jrie.put.pod.bazeries.cipher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import pl.allegro.finance.tradukisto.ValueConverters
import java.util.*

typealias Receiver<R> = suspend (Flow<Char>) -> R

internal class Bazeries (
    private val key: Int,
    private val useFullNumberName: Boolean = false,
    useExtendedAlphabet: Boolean = false
) {

    private val alphabet = if(useExtendedAlphabet) EXTENDED_ALPHABET else ALPHABET
    private val width = if(useExtendedAlphabet) EXTENDED_WIDTH else WIDTH
    private val height = if(useExtendedAlphabet) EXTENDED_HEIGHT else HEIGHT
    private val alphabetMatrix = buildAlphabet(alphabet, width, height)

    private val keyword = run {
        if(useFullNumberName) ValueConverters.ENGLISH_INTEGER.asWords(key).inAlphabet()
        else key.toString()
            .map { numbers[it] }
            .distinct()
            .joinToString(separator = "")
    }

    private val codeMatrix = keyword.plus(alphabet)
        .toList()
        .distinct()
        .joinToString(separator = "")
        .let { buildCipher(it, width) }

    private var chunkSizes = mutableListOf<Int>().apply {
        key.toString()
            .map { "$it".toInt() }
            .filter { it > 1 }
            .onEach { add(it) }
    }
    private var chunkSizeIterator = chunkSizes.iterator()
    private var chunkSize = chunkSizeIterator.next()
    private fun chunker() {
        if (!chunkSizeIterator.hasNext()) chunkSizeIterator = chunkSizes.iterator()
        chunkSize = chunkSizeIterator.next()
    }

    private fun findInMatrix(matrix: Array<Array<Char>>, l: Char): Pair<Int, Int> {
        matrix.forEachIndexed { y, line ->
            line.forEachIndexed { x, letter -> if (letter == l) return y to x }
        }
        return 0 to 0
    }

    private fun mapToCode(l: Char) = findInMatrix(alphabetMatrix, l).let { codeMatrix[it.first][it.second] }
    private fun mapToAlphabet(l: Char) = findInMatrix(codeMatrix, l).let { alphabetMatrix[it.first][it.second] }

    fun <R> encode(textSequence: Sequence<Char>, receiver: Receiver<R>) = textSequence.process(::mapToCode, receiver)
    fun <R> decode(textStream: Sequence<Char>, receiver: Receiver<R>) = textStream.process(::mapToAlphabet, receiver)

    private fun <R> Sequence<Char>.process(mapper: (Char) -> Char, receiver: Receiver<R>) = runBlocking {
        println("key: $key")
        println("keyword: $keyword")
        flow {
            chunkText(getAndFilterCharacters(this@process)).collect { chunk ->
                while (chunk.isNotEmpty()) {
                    emit(mapper(chunk.pop()))
                }
            }
        }.let { receiver(it) }
    }

    private fun chunkText(text: Flow<Char>) = flow {
        var stack = Stack<Char>()
        var counter = 0
        text.collect {
            if (counter >= chunkSize) {
                emit(stack)
                stack = Stack()
                counter = 0
                chunker()
            }
            stack.push(it)
            counter++
        }
        if(stack.isNotEmpty()) emit(stack)
    }

    private fun getAndFilterCharacters(text: Sequence<Char>) = text.filter { alphabet.contains(it) } .asFlow()

    private fun String.inAlphabet() = filter { alphabet.contains(it) }

    companion object {
        private const val ALPHABET = "abcdefghiklmnopqrstuvwxyz"
        private const val EXTENDED_ALPHABET = "aąbcćdeęfghijklłmnńoópqrsśtuvwxyzźż" +
                "AĄBCĆDEĘFHGIJKLŁMNŃOÓPQRSŚTUVWXYZŹŻ" +
                " \n" +
                "!@#\$%^&*()-=_+[]{}|\\;:'\",.<>/?`~" +
                "1234567890"
        private const val WIDTH = 5
        private const val HEIGHT = 5
        private const val EXTENDED_WIDTH = 6
        private const val EXTENDED_HEIGHT = 19

        private val numbers = mapOf(
            '0' to "zero",
            '1' to "one",
            '2' to "two",
            '3' to "three",
            '4' to "four",
            '5' to "five",
            '6' to "six",
            '7' to "seven",
            '8' to "eight",
            '9' to "nine"
        )

        private inline fun buildMatrix(
            letters: String, width: Int, index: (Int, Int) -> Int) = Array(letters.length / width
        ) { y -> Array(width) { x -> letters[index(x, y)] } }.also {
            it.forEach { a ->
                a.forEach { l -> print("$l ") }
                println()
            }
            println()
        }

        private fun buildAlphabet(letters: String, width: Int, height: Int)
                = buildMatrix(letters, width) { x, y -> (y + (x * height)) }
        private fun buildCipher(letters: String, width: Int)
                = buildMatrix(letters, width) { x, y -> x + (y * width) }

        fun randomKey() = (1..999_999).random()
    }
}
