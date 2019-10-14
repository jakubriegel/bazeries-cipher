package eu.jrie.put.pod.bazeries.cipher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import pl.allegro.finance.tradukisto.ValueConverters
import java.io.InputStream
import java.util.*

typealias Receiver<R> = suspend (Flow<Char>) -> R

internal class Bazeries (
    private val key: Int,
    private val useExtendedAlphabet: Boolean = false,
    private val useFullNumberName: Boolean = false
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
        .let { buildCipher(it, width, height) }

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

    private fun chunkText(text: InputStream) = flow {
        text.use { input ->
            var hasNext = true
            while (hasNext) {
                Stack<Char>().apply {
                    repeat(chunkSize) {
                        if (input.available() > 0) this.push(input.read().toChar())
                        else hasNext = false
                    }
                }.let { emit(it) }
                chunker()
            }
        }
    }

    fun <R> encode(textStream: InputStream, receiver: Receiver<R>) = textStream.process(::mapToCode, receiver)
    fun <R> decode(textStream: InputStream, receiver: Receiver<R>) = textStream.process(::mapToAlphabet, receiver)

    private fun String.inAlphabet() = filter { alphabet.contains(it) }

    private fun <R> InputStream.process(mapper: (Char) -> Char, receiver: Receiver<R>) = runBlocking {
        println("key: $key")
        println("keyword: $keyword")
        flow {
            chunkText(this@process).collect { chunk: Stack<Char> ->
                while (chunk.isNotEmpty()) {
                    emit(mapper(chunk.pop()))
                }
            }
        }.let { receiver(it) }
    }

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

        private inline fun buildMatrix(letters: String, width: Int, index: (Int, Int) -> Int) = Array(letters.length / width) { y ->
            Array(width) { x -> letters[index(x, y)].also { println(" $x $y $it") } }
        }.also {
            it.forEach { a ->
                a.forEach { l -> print("$l ") }
                println()
            }
            println()
        }

        private fun buildAlphabet(letters: String, width: Int, height: Int)
                = buildMatrix(letters, width) { x, y -> (y + (x * height)).also { print(it) } }
        private fun buildCipher(letters: String, width: Int, height: Int)
                = buildMatrix(letters, width) { x, y -> x + (y * width) }
    }
}
