package eu.jrie.put.pod.bazeries

import eu.jrie.put.pod.bazeries.user.cli
import eu.jrie.put.pod.bazeries.user.gui
import kotlinx.coroutines.ExperimentalCoroutinesApi

private const val CLI_OPTION = "CLI"
private const val GUI_OPTION = "GUI"

@ExperimentalCoroutinesApi
fun main(args: Array<String>) {
    interfaceStart(if(args.isNotEmpty()) args else arrayOf(GUI_OPTION))
}

@ExperimentalCoroutinesApi
fun interfaceStart(args: Array<String>) {
    when(args.first().toUpperCase()) {
        CLI_OPTION -> cli(args.drop(1))
        GUI_OPTION -> gui()
        else -> print("unknown mode")
    }
}
