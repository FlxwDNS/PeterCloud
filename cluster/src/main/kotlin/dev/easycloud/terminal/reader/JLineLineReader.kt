package dev.easycloud.terminal.reader

import dev.easycloud.logger
import dev.easycloud.terminal.command.CommandService
import org.jline.reader.LineReader
import org.jline.reader.UserInterruptException
import kotlin.system.exitProcess

class JLineLineReader(val lineReader: LineReader, val prompt: String = logger.colorText("<gray>easycloud<white>@<gray>${System.getProperty("version")}<white>:<reset> ")) {
    val commandService = CommandService()

    fun start(): Thread {
        val thread = Thread.startVirtualThread {
            while (true) {
                val line = lineReader.readLine(prompt)
                if (line.isNotBlank()) {
                    commandService.run(line)
                }
            }
        }
        thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, throwable ->
            if(throwable !is UserInterruptException) {
                throwable.printStackTrace()
            }
            exitProcess(0)
        }
        logger.debug("debug.terminalReadingStarted")
        return thread
    }
}