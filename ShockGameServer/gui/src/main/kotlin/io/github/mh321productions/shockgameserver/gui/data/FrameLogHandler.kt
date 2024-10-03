package io.github.mh321productions.shockgameserver.gui.data

import io.github.mh321productions.shockgameserver.gui.widgets.MainFrame
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.util.logging.Handler
import java.util.logging.LogRecord

class FrameLogHandler(private val frame: MainFrame) : Handler() {

    override fun publish(record: LogRecord) {
        val builder = StringBuilder()

        builder.append('[')
        builder.append(record.instant.toSimpleTimeString())
        builder.append("] [")
        builder.append(record.level)
        builder.append("] ")
        builder.append(record.message)

        if (record.thrown != null) {
            builder.append('\n')
            builder.append(" ".repeat(14 + record.level.toString().length))
            builder.append(record.thrown.message)
            builder.append(" (${record.thrown.javaClass.simpleName})")
        }

        frame.addLog(builder.toString())
    }

    override fun flush() {}

    override fun close() {}
}

fun Instant.toSimpleTimeString(): String {
    val time = LocalTime.ofInstant(this, ZoneId.systemDefault())
    return String.format("%02d:%02d:%02d", time.hour, time.minute, time.second)
}