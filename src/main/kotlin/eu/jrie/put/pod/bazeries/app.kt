package eu.jrie.put.pod.bazeries

import eu.jrie.put.pod.bazeries.user.cli
import eu.jrie.put.pod.bazeries.user.gui

private const val CLI_OPTION = "CLI"
private const val GUI_OPTION = "GUI"

fun main(args: Array<String>) {
    interfaceStart(args)
}

fun interfaceStart(args: Array<String>) {
    println(args.joinToString())
    when(args.first().toUpperCase()) {
        CLI_OPTION -> cli(args.drop(1))
        GUI_OPTION -> gui()
        else -> print("unknown mode")
    }
}
