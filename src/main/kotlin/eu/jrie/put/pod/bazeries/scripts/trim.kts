package eu.jrie.put.pod.bazeries.scripts

import java.io.File

val source = "text_original.txt"
val output = "text.txt"
val extended = false

val alphabet = "abcdefghiklmnopqrstuvwxyz".toList()
val extendedAlphabet = "aąbcćdeęfghijklłmnńoópqrsśtuvwxyzźż \n.,!?".toList()
val pathToResources = "../../../../../../../resources"

val file = File("$pathToResources/$source")
val base = if(extended) extendedAlphabet else alphabet

val result = file.readText()
    .map { it.toLowerCase() }
    .filter { base.contains(it) }
    .joinToString(separator = "")

File("$pathToResources/$output").writeText(result)
