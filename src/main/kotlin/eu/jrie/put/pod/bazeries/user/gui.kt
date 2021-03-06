package eu.jrie.put.pod.bazeries.user

import com.sandec.mdfx.MDFXNode
import eu.jrie.put.pod.bazeries.cipher.cipher
import eu.jrie.put.pod.bazeries.cipher.decipher
import javafx.geometry.Side
import javafx.scene.control.TextArea
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tornadofx.App
import tornadofx.CssBox
import tornadofx.Dimension
import tornadofx.Drawer
import tornadofx.DrawerItem
import tornadofx.View
import tornadofx.action
import tornadofx.attachTo
import tornadofx.button
import tornadofx.checkbox
import tornadofx.drawer
import tornadofx.hbox
import tornadofx.label
import tornadofx.launch
import tornadofx.scrollpane
import tornadofx.style
import tornadofx.textarea
import tornadofx.textfield
import tornadofx.vbox
import java.io.File

fun gui() {
    launch<AppGUI>()
}

class AppGUI : App(AppView::class) {
    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }
}

enum class Mode { TEXT, FILE }

class AppView : View() {

    private var mode = Mode.TEXT

    init {
        title = "Bazeries Cipher"
    }

    @ExperimentalCoroutinesApi
    override val root = drawer {
        configDrawer()
        cipherItem().also { it.expanded = true }
        algorithmItem()
        aboutItem()
    }

    private fun Drawer.configDrawer() {
        stylesheets.add("/com/sandec/mdfx/mdfx-default.css")
        dockingSide = Side.TOP
        style {
            minWidth = Dimension(500.0, Dimension.LinearUnits.px)
            minHeight = Dimension(375.0, Dimension.LinearUnits.px)
        }
    }

    private lateinit var sourceFile: File
    private lateinit var codedContent: TextArea
    @ExperimentalCoroutinesApi
    private fun Drawer.cipherItem() = drawerItem("cipher") {
        hbox {
            vbox {
                label("Text to encode/decode:")
                val textInput = textarea()
                button("Use file") {
                    action {
                        sourceFile = FileChooser().showOpenDialog(currentWindow)
                        mode = Mode.FILE
                        text = sourceFile.name
                    }
                }
                label("Key <1, 999999> (empty for random):")
                val keyInput = textfield()
                val fullNumberKeyNameFlag = checkbox("Use full number name key")
                val extendedAlphabetFlag = checkbox("Use extended alphabet")
                hbox {
                    button("Encode") {
                        action {
                            encodeAction(
                                textInput.text, keyInput.characters,
                                fullNumberKeyNameFlag.isSelected, extendedAlphabetFlag.isSelected
                            )
                        }
                    }
                    button("Decode") {
                        action {
                            decodeAction(
                                textInput.text, keyInput.characters,
                                fullNumberKeyNameFlag.isSelected, extendedAlphabetFlag.isSelected
                            )
                        }
                    }
                }
                style {
                    minWidth = Dimension(240.0, Dimension.LinearUnits.px)
                    maxWidth = Dimension(240.0, Dimension.LinearUnits.px)
                    padding = CssBox(
                        top = Dimension(0.0, Dimension.LinearUnits.px),
                        right = Dimension(10.0, Dimension.LinearUnits.px),
                        bottom = Dimension(0.0, Dimension.LinearUnits.px),
                        left = Dimension(0.0, Dimension.LinearUnits.px)
                    )
                }
            }
            vbox {
                label("Result:")
                codedContent = textarea {
                    isEditable = false
                }
                style {
                    minWidth = Dimension(240.0, Dimension.LinearUnits.px)
                    maxWidth = Dimension(240.0, Dimension.LinearUnits.px)
                    padding = CssBox(
                        top = Dimension(0.0, Dimension.LinearUnits.px),
                        right = Dimension(0.0, Dimension.LinearUnits.px),
                        bottom = Dimension(0.0, Dimension.LinearUnits.px),
                        left = Dimension(10.0, Dimension.LinearUnits.px)
                    )
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun encodeAction(text: CharSequence, key: CharSequence, useFullNumberNameKey: Boolean, useExtendedAlphabet: Boolean) {
        codedContent.text = when(mode) {
            Mode.TEXT -> {
                println(text.toString().toByteArray(Charsets.UTF_8).toString(Charsets.UTF_8))
                when(key.isBlank()) {
                    true ->  cipher(
                        text.toString().toByteArray(Charsets.UTF_8).toString(Charsets.UTF_8), useFullNumberKeyName = useFullNumberNameKey, useExtendedAlphabet = useExtendedAlphabet
                    )
                    false -> cipher(
                        text.toString().toByteArray(Charsets.UTF_8).toString(Charsets.UTF_8).toByteArray(Charsets.UTF_8).toString(Charsets.UTF_8), "$key".toInt(), useFullNumberNameKey, useExtendedAlphabet
                    )
                }
            }
            Mode.FILE -> {when(key.isBlank()) {
                true -> cipher(
                    sourceFile, useFullNumberKeyName = useFullNumberNameKey, useExtendedAlphabet = useExtendedAlphabet
                ).name
                false -> cipher(
                    sourceFile, "$key".toInt(), useFullNumberNameKey, useExtendedAlphabet
                ).name
            }}
        }
    }

    @ExperimentalCoroutinesApi
    private fun decodeAction(text: CharSequence, key: CharSequence, useFullNumberNameKey: Boolean, useExtendedAlphabet: Boolean) {
        codedContent.text = decipher(text.toString(), "$key".toInt(), useFullNumberNameKey, useExtendedAlphabet)
        when(mode) {
            Mode.TEXT -> codedContent.text = decipher(
                text.toString(), "$key".toInt(), useFullNumberNameKey, useExtendedAlphabet
            )
            Mode.FILE -> codedContent.text = decipher(
                sourceFile, "$key".toInt(), useFullNumberNameKey, useExtendedAlphabet
            ).absolutePath
        }
    }

    private fun Drawer.algorithmItem() = drawerItem("algorithm") {
        textContent("algorithm.md")
    }

    private fun Drawer.aboutItem() = drawerItem("about") {
        textContent("about.md")
    }

    private fun DrawerItem.textContent(name: String) {
        scrollpane {
            this::class.java.getResource("/textcontent/$name")
                .readText()
                .let { MDFXNode(it).attachTo(this) {
                    style {
                        maxWidth = Dimension(460.0, Dimension.LinearUnits.px)
                    }
                } }

            style {
                backgroundColor += Color.WHITE
            }
        }
    }

    private fun Drawer.drawerItem(name: String, content: DrawerItem.() -> Unit) = item(name) {
        style {
            padding = CssBox(
                top = Dimension(15.0, Dimension.LinearUnits.px),
                right = Dimension(20.0, Dimension.LinearUnits.px),
                bottom = Dimension(15.0, Dimension.LinearUnits.px),
                left = Dimension(20.0, Dimension.LinearUnits.px)
            )
            minWidth = Dimension(500.0, Dimension.LinearUnits.px)

        }
        content()
    }
}
