package eu.jrie.put.pod.bazeries.cipher

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import java.io.File

@ExperimentalCoroutinesApi
fun cipher(
    text: String, key: Int = Bazeries.randomKey(), useFullNumberKeyName: Boolean = true, useExtendedAlphabet: Boolean = false
): String = cipherText(text, key, useFullNumberKeyName, useExtendedAlphabet, ::cipher)

fun cipher(
    textFile: File, key: Int = Bazeries.randomKey(), useFullNumberKeyName: Boolean = true, useExtendedAlphabet: Boolean = false
): File = cipherFile(textFile, key, useFullNumberKeyName, useExtendedAlphabet, ::cipher)

private fun <R> cipher(
    text: Sequence<Char>, key: Int, useFullNumberKeyName: Boolean, useExtendedAlphabet: Boolean,
    receiver: suspend (Flow<Char>) -> R
) = Bazeries(key, useFullNumberKeyName, useExtendedAlphabet).encode(text, receiver)

@ExperimentalCoroutinesApi
fun decipher(
    text: String, key: Int, useFullNumberKeyName: Boolean = true, useExtendedAlphabet: Boolean = false
): String = cipherText(text, key, useFullNumberKeyName, useExtendedAlphabet, ::decipher)

fun decipher(
    textFile: File, key: Int, useFullNumberKeyName: Boolean = true, useExtendedAlphabet: Boolean = false
): File = cipherFile(textFile, key, useFullNumberKeyName, useExtendedAlphabet, ::decipher)

private fun <R> decipher(
    stream: Sequence<Char>, key: Int, useFullNumberKeyName: Boolean, useExtendedAlphabet: Boolean,
    receiver: suspend (Flow<Char>) -> R
) = Bazeries(key, useFullNumberKeyName, useExtendedAlphabet).decode(stream, receiver)

@ExperimentalCoroutinesApi
private fun cipherText(
    text: String, key: Int, useFullNumberKeyName: Boolean, useExtendedAlphabet: Boolean,
    cipherAction: (Sequence<Char>, Int, Boolean, Boolean, suspend (Flow<Char>) -> Unit) -> Unit
): String = CharArray(text.length).apply {
    println("coding: $this")
    cipherAction(text.asSequence(), key, useFullNumberKeyName, useExtendedAlphabet) { flow ->
        flow.collectIndexed { i, value -> set(i, value) }
    }
} .let { result -> String(result).also { println("result: $it") } }


private fun cipherFile(
    textFile: File, key: Int = Bazeries.randomKey(), useFullNumberKeyName: Boolean, useExtendedAlphabet: Boolean,
    cipherAction: (Sequence<Char>, Int, Boolean, Boolean, suspend (Flow<Char>) -> Unit) -> Unit
): File = File(textFile.absolutePath.plus(".bz"))
    .apply { delete() }
    .apply {
        cipherAction(textFile.charSequence(), key, useFullNumberKeyName, useExtendedAlphabet) {
            saveResult(it)
        } .also { println("result file: $name") }
    }

fun File.charSequence(): Sequence<Char> {
    return bufferedReader(Charsets.UTF_8).lineSequence().let { lines ->
        sequence {
            lines.forEach { l ->
                l.forEach { yield(it) }
                yield('\n')
            }
        }
    }
}

private const val WRITE_PACKET_SIZE = 512
private suspend fun File.saveResult(flow: Flow<Char>) {
    CharArray(WRITE_PACKET_SIZE).let { buffer ->
        var size = 0
        flow.collect { l ->
            buffer[size++] = l
            if(size >= WRITE_PACKET_SIZE) {
                this.writer().append()
                appendText(String(buffer), Charsets.UTF_8)
                size = 0
            }
        }
        if(size > 0) {
            appendText(String(buffer.sliceArray(0 until size)), Charsets.UTF_8)
        }
    }
}
