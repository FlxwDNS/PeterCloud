package dev.easycloud.terminal

import dev.easycloud.terminal.reader.JLineLineReader
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

class JLineTerminal {
    val terminal: Terminal = TerminalBuilder.builder()
        .system(true)
        .build()

    val reader: LineReader = LineReaderBuilder.builder()
        .terminal(this.terminal)
        .build()

    fun start() {
        JLineLineReader(this.reader).start()
    }
}