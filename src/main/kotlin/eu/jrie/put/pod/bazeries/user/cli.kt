package eu.jrie.put.pod.bazeries.user

import eu.jrie.put.pod.bazeries.cipher.cipher
import eu.jrie.put.pod.bazeries.cipher.decipher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File

@ExperimentalCoroutinesApi
fun cli(args: List<String>) {
    when(args.first()) {
        "-d" -> processDecryption(args.drop(1))
        else -> processEncryption(args)
    }
}

@ExperimentalCoroutinesApi
private fun processEncryption(args: List<String>) {
    when(args.first()) {
        "-f" -> cipher(File(args[1]), args[2].toInt())
        else -> cipher(args[0], args[1].toInt())
    }
}

@ExperimentalCoroutinesApi
private fun processDecryption(args: List<String>) {
    when(args.first()) {
        "-f" -> decipher(File(args[1]), args[2].toInt())
        else -> decipher(args[0], args[1].toInt())
    }
}
