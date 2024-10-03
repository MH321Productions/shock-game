package io.github.mh321productions.shockgameserver.server.game

import io.github.mh321productions.shockgameserver.server.network.managers.ServerNetworkManager
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

class Server(handler: Handler) {

    var activeGame: Game? = null
    val hasActiveGame = activeGame != null
    val conn = ServerNetworkManager(this)
    val players = mutableListOf<Player>()
    val log: Logger

    init {
        LogManager.getLogManager().reset()
        log = Logger.getLogger("ShockServer")
        log.addHandler(handler)
    }

    fun createGame(info: GameInfo): Boolean {
        if (hasActiveGame) {
            log.severe("Couldn't create game: A game is already running")
            return false
        }

        try {
            info.validate()
        } catch (e: IllegalArgumentException) {
            log.log(Level.SEVERE, "Couldn't create game:", e)
        }

        activeGame = Game(this, info)
        return true
    }
}