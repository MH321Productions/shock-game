package io.github.mh321productions.shockgameserver.server.network.managers

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.mh321productions.shockgameserver.server.game.Server
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.SocketException
import java.util.logging.Level
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.InflaterInputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


class PlayerConnectionsAcceptorThread(private val server: Server, private val socket: ServerSocket) : Thread("PlayerConnectionsAcceptor") {

    companion object {
        const val minimumClientVersion = "0.0.1"
        const val expectedClientMessage = "SHOCK!-Client"

        const val serverVersion = "0.0.1"
        const val serverMessage = "SHOCK!-Server $serverVersion"
    }

    private val questions: List<String> =
        PlayerConnectionsAcceptorThread::class.java.getResourceAsStream("/Fragen.txt")?.reader()?.readLines() ?: listOf()
    private val answers: List<String> =
        PlayerConnectionsAcceptorThread::class.java.getResourceAsStream("/Antworten.txt")?.reader()?.readLines() ?: listOf()

    private val dataJson: JsonObject

    init {
        dataJson = JsonObject()
        dataJson.add("questions", questions.toJsonArray())
        dataJson.add("answers", answers.toJsonArray())
    }

    override fun run() {
        try {
            while (socket.isBound) {
                val client = socket.accept()

                //Test
                server.log.info("Client connected: ${client.inetAddress.hostAddress}")

                //Read
                val reader = BufferedReader(InputStreamReader(client.getInputStream(), Charsets.UTF_8))
                val gson = GsonBuilder().setPrettyPrinting().create()
                var line : String
                for (i in 1 .. 2) {
                    line = reader.readLine() ?: break
                    val root = JsonParser.parseString(line).asJsonObject
                    server.log.info("Received json: ${gson.toJson(root)}")
                }

                //Write
                val writer = OutputStreamWriter(client.outputStream, Charsets.UTF_8)
                val first = JsonObject()
                first.addProperty("name", "Nice")
                first.addProperty("email", "nice@nice.com")
                first.add("numbers", gson.toJsonTree(arrayOf(1, 2, 3)))

                val second = JsonObject()
                second.addProperty("name", "Nicer")
                second.addProperty("email", "nicer@nice.com")
                second.add("numbers", gson.toJsonTree(arrayOf(4, 5, 6)))

                server.log.info("Writing first ($first)")
                writer.write("$first\n")
                writer.flush()

                server.log.info("Writing second")
                writer.write(gson.toJson(second))
                writer.flush()

                server.log.info("Writing questions and answers")
                writer.write(dataJson.toString())
                writer.flush()

                client.close()
                server.log.info("Client closed")
            }
        } catch (e: SocketException) {
            if (e.message != "Socket closed") server.log.log(Level.WARNING, "No more player connections can be accepted:", e)
        } catch (e: IOException) {
            server.log.log(Level.SEVERE, "Error while accepting player connection:", e)
        }
    }

    private fun List<String>.toJsonArray() : JsonArray {
        val array = JsonArray()
        this.forEach { array.add(it) }

        return array
    }
}