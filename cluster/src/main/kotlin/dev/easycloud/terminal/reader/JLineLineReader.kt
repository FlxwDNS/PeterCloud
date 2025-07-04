package dev.easycloud.terminal.reader

import dev.easycloud.logger
import dev.easycloud.terminal.command.CommandService
import org.jline.reader.LineReader

class JLineLineReader(val lineReader: LineReader, val prompt: String = logger.colorText("<gray>easycloud<white>@<gray>${System.getProperty("version")}<white>:<reset> ")) {
    val commandService = CommandService()

    fun start() {
        Thread {
            while (true) {
                val line = this.lineReader.readLine(this.prompt)
                if (line.isNotBlank()) {
                    commandService.run(line)
                }
            }
        }.start()
    }
}