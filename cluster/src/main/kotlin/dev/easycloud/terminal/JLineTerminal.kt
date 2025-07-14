package dev.easycloud.terminal

import dev.easycloud.localisation
import dev.easycloud.terminal.reader.JLineLineReader
import org.jline.reader.LineReader
import org.jline.reader.impl.LineReaderImpl
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.utils.AttributedString
import org.jline.utils.InfoCmp

class JLineTerminal {
    val terminal: Terminal = TerminalBuilder.builder()
        .system(true)
        .build()

    val reader: LineReader = object : LineReaderImpl(terminal) {
        override fun cleanup() {
            buf.clear()
            post = null
            prompt = AttributedString("")

            redisplay(true)

            terminal.puts(InfoCmp.Capability.keypad_local)
            terminal.trackMouse(Terminal.MouseTracking.Off)

            if (isSet(LineReader.Option.BRACKETED_PASTE)) {
                terminal.writer().write(BRACKETED_PASTE_OFF)
            }

            flush()
            history.moveToEnd()
        }
    }

    fun run() {
        JLineLineReader(reader).start()

    }

    fun shutdown() {
        terminal.close()
    }
}