package eu.jrie.put.pod.bazeries.cipher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.io.File
import java.io.InputStream

fun cipher(text: String, key: Int = (1..999_999).random()): String {
    println("encoding: $text")
    val result = CharArray(text.length).apply {
        var i = 0
        cipher(text.byteInputStream(), key) { flow ->
            flow.collect { set(i++, it) }
        }
    }
    return String(result).also { println("result: $it") }
}

fun cipher(textFile: File, key: Int = (1..999_999).random()): File {
    println("encoding file: ${textFile.name}")
    val resultFile = File(textFile.absolutePath.plus(".enc"))
    return cipher(textFile.inputStream(), key) {
        saveResultToFile(it, resultFile)
    } .also { println("result file: ${resultFile.name}") }
}

private fun <R> cipher(stream: InputStream, key: Int, receiver: suspend (Flow<Char>) -> R)
        = Bazeries(key).encode(stream, receiver)

fun decipher(text: String, key: Int = (1..999_999).random()): String {
    println("decoding: $text")
    val result = CharArray(text.length).apply {
        var i = 0
        decipher(text.byteInputStream(), key) { flow ->
            flow.collect { set(i++, it) }
        }
    }
    return String(result).also { println("result: $it") }
}

fun decipher(textFile: File, key: Int = (1..999_999).random()): File {
    println("decoding file: ${textFile.name}")
    val resultFile = File(textFile.absolutePath.plus(".dec"))
    return decipher(textFile.inputStream(), key) {
        saveResultToFile(it, resultFile)
    } .also { println("result file: ${resultFile.name}") }
}

fun <R> decipher(stream: InputStream, key: Int, receiver: suspend (Flow<Char>) -> R)
        = Bazeries(key).decode(stream, receiver)

const val WRITE_PACKET_SIZE = 256
private suspend fun saveResultToFile(flow: Flow<Char>, file: File): File {
    ByteArray(WRITE_PACKET_SIZE).let { buffer ->
        var size = 0
        flow.collect { l ->
            buffer[size++] = l.toByte()
            if(size >= WRITE_PACKET_SIZE) {
                file.appendBytes(buffer)
                size = 0
            }
        }
        if(size > 0) {
            file.appendBytes(buffer.sliceArray(0 until size))
        }
    }
    return file
}
