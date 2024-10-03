package io.github.mh321productions.shockgameserver.gui

import io.github.mh321productions.shockgameserver.gui.widgets.MainFrame
import java.awt.EventQueue

fun main() {
    EventQueue.invokeLater {
        val frame = MainFrame()
        frame.isVisible = true
    }
}