package eu.jrie.put.pod.bazeries.user

import eu.jrie.put.pod.bazeries.cipher.cipher
import javafx.geometry.Side
import tornadofx.App
import tornadofx.CssBox
import tornadofx.Dimension
import tornadofx.Drawer
import tornadofx.DrawerItem
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.drawer
import tornadofx.label
import tornadofx.launch
import tornadofx.style
import tornadofx.textfield

fun gui() {
    launch<AppGUI>()
}

class AppGUI : App(AppView::class)

class AppView : View() {

    init {
        title = "Bazeries Cipher"
    }

    override val root = drawer {
        configDrawer()
        cipherItem().also { it.expanded = true }
        algorithmItem()
        aboutItem()
    }

    private fun Drawer.configDrawer() {
        dockingSide = Side.TOP
        style {
            minWidth = Dimension(500.0, Dimension.LinearUnits.px)
            minHeight = Dimension(350.0, Dimension.LinearUnits.px)
        }
    }

    private fun Drawer.cipherItem() = drawerItem("cipher") {
        label("Input:")
        val input = textfield {
            action {
                cipherAction(characters)
            }
        }
        button("Encode") {
            action {
                cipherAction(input.characters)
            }
        }
    }

    private fun cipherAction(text: CharSequence) {
        println(cipher(text.toString()))
    }

    private fun Drawer.algorithmItem()  = drawerItem("algorithm") {
        label("cool2")
    }

    private fun Drawer.aboutItem() = drawerItem("about") {
        label("cool3")
    }

    private fun Drawer.drawerItem(name: String, content: DrawerItem.() -> Unit) = item(name) {
        style {
            padding = CssBox(
                top = Dimension(15.0, Dimension.LinearUnits.px),
                right = Dimension(20.0, Dimension.LinearUnits.px),
                bottom = Dimension(15.0, Dimension.LinearUnits.px),
                left = Dimension(20.0, Dimension.LinearUnits.px)
            )
        }
        content()
    }

}

