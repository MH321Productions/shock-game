package io.github.mh321productions.shockgameserver.gui.widgets

import io.github.mh321productions.shockgameserver.gui.data.FrameLogHandler
import io.github.mh321productions.shockgameserver.server.game.Server
import net.miginfocom.swing.MigLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder

class MainFrame : JFrame() {

    private val server = Server(FrameLogHandler(this))

    private val spEvents : JScrollPane
    private val panelServer : JPanel
    private val panelGame : JPanel
    private val txtEvents : JTextArea
    private val btnStartStop : JButton

    init {
        title = "Shock Game"
        size = Dimension(800, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        contentPane = JPanel()
        contentPane.layout = MigLayout("", "[grow][grow 40]", "[grow][grow]")

        //Events
        spEvents = JScrollPane()
        spEvents.border = TitledBorder(LineBorder(Color.black), "Events", TitledBorder.LEADING, TitledBorder.TOP, null, Color.black)

        txtEvents = JTextArea()
        txtEvents.lineWrap = false
        txtEvents.wrapStyleWord = false

        spEvents.setViewportView(txtEvents)
        contentPane.add(spEvents, "cell 0 0 1 2, grow")

        //Server
        panelServer = JPanel()
        panelServer.border = TitledBorder(LineBorder(Color.black), "Server", TitledBorder.LEADING, TitledBorder.TOP, null, Color.black)
        panelServer.layout = MigLayout("", "[grow][grow]", "[][]")
        contentPane.add(panelServer, "cell 1 0, grow")

        btnStartStop = JButton("Start")
        btnStartStop.addActionListener {onPressStartStop()}
        panelServer.add(btnStartStop, "cell 0 0 2, grow")

        //Game
        panelGame = JPanel()
        panelGame.border = TitledBorder(LineBorder(Color.black), "Game", TitledBorder.LEADING, TitledBorder.TOP, null, Color.black)
        contentPane.add(panelGame, "cell 1 1, grow")
    }

    fun addLog(log: String) {
        txtEvents.append("$log\n")
    }

    private fun onPressStartStop() {
        if (btnStartStop.text == "Start") {
            //Test
            if (server.conn.create(8080)) btnStartStop.text = "Stop"

        } else if (btnStartStop.text == "Stop") {
            server.conn.stop()
            btnStartStop.text = "Start"
        }
    }
}