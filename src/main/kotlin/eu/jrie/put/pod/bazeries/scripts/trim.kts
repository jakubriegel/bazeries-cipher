package eu.jrie.put.pod.bazeries.scripts

import java.io.File

val source = "text_original.txt"
val output = "text_extended.txt"
val extended = true

val alphabet = "abcdefghiklmnopqrstuvwxyz".toList()
val extendedAlphabet = (
            "aąbcćdeęfghijklłmnńoópqrsśtuvwxyzźż" +
            "AĄBCĆDEĘFHGIJKLŁMNŃOÓPQRSŚTUVWXYZŹŻ" +
            " \n" +
            "!@#\$%^&*()-=_+[]{}|\\;:'\",.<>/?`~" +
            "1234567890"
        ).toList()

val pathToResources = "../../../../../../../resources"

val file = File("$pathToResources/$source")
val base = if(extended) extendedAlphabet else alphabet

val result = file.readText()
    .map { if(!extended) it.toLowerCase() else it }
    .filter { base.contains(it) }
    .joinToString(separator = "")

File("$pathToResources/$output").writeText(result)
