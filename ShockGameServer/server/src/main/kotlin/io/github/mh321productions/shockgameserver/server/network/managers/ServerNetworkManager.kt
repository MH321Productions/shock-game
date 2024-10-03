package io.github.mh321productions.shockgameserver.server.network.managers

import io.github.mh321productions.shockgameserver.server.game.Server
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.logging.Level

class ServerNetworkManager(private val server: Server) {

    private var socket = ServerSocket()
    val isActive = socket.isBound
    private var acceptor = PlayerConnectionsAcceptorThread(server, socket)

    fun create(port: Int): Boolean {
        try {
            socket.bind(InetSocketAddress(port))
            server.log.info("Accepting connections")
            acceptor.start()
            return true
        } catch (e: IOException) {
            server.log.log(Level.SEVERE, "Couldn't start server:", e)
            return false
        }
    }

    fun stop() {
        if (!socket.isBound) return

        socket.close()
        server.log.info("Server stopped")

        //Configure server for restart
        socket = ServerSocket()
        acceptor = PlayerConnectionsAcceptorThread(server, socket)
    }
}