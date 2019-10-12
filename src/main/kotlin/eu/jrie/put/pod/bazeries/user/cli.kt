package eu.jrie.put.pod.bazeries.user

import eu.jrie.put.pod.bazeries.cipher.cipher
import eu.jrie.put.pod.bazeries.cipher.decipher
import kotlinx.coroutines.flow.collect
import java.io.File

fun cli(args: List<String>) {
    when(args.first()) {
        "-d" -> processDecryption(args)
        else -> processEncryption(args)
    }
}

private fun processEncryption(args: List<String>) {
    when(args.first()) {
        "-f" -> cipher(File(args[1]), args[2].toInt())
        else -> cipher(args[0], args[1].toInt())
    }
}

private fun processDecryption(args: List<String>) {
    when(args.first()) {
        "-f" -> cipher(File(args[1]), args[2].toInt())
        else -> decipher(args[1].byteInputStream(), 900_004) { it.collect { l -> print(l)} }
    }
}