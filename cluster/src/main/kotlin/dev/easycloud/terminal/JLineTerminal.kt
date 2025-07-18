package dev.easycloud.terminal

import dev.easycloud.logger
import dev.easycloud.terminal.command.CommandService
import dev.easycloud.terminal.command.completer.CommandCompleter
import dev.easycloud.terminal.command.completer.CommandSuggestionWidget
import dev.easycloud.terminal.reader.JLineLineReader
import org.jline.reader.LineReader
import org.jline.reader.impl.LineReaderImpl
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.utils.AttributedString
import org.jline.utils.InfoCmp
import org.jline.widget.AutosuggestionWidgets
import org.jline.widget.TailTipWidgets
import java.nio.charset.StandardCharsets

class JLineTerminal {
    val terminal: Terminal = TerminalBuilder.builder()
        .system(true)
        .encoding(StandardCharsets.UTF_8)
        .dumb(true)
        .build()

    val reader: LineReaderImpl = object : LineReaderImpl(terminal) {
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
    private lateinit var readerThread: Thread

    val commandService = CommandService()

    fun run() {
        logger.debug("debug.terminalReading")

        reader.completer = CommandCompleter(commandService)

        CommandSuggestionWidget(reader).enable()
        readerThread = JLineLineReader(reader, commandService).start()
    }

    fun shutdown() {
        logger.debug("debug.terminalShuttingDown")

        readerThread.interrupt()
        terminal.close()
    }

    fun write(message: String) {
        terminal.puts(InfoCmp.Capability.carriage_return)
        terminal.writer().println(message)
        terminal.flush()

        if(reader.isReading) {
            reader.callWidget(LineReader.REDRAW_LINE)
            reader.callWidget(LineReader.REDISPLAY)
        }
    }
}