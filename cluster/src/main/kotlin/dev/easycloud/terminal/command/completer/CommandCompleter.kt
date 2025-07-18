package dev.easycloud.terminal.command.completer

import dev.easycloud.terminal.command.CommandService
import org.jline.reader.Candidate
import org.jline.reader.Completer
import org.jline.reader.LineReader
import org.jline.reader.ParsedLine

class CommandCompleter(val commandService: CommandService): Completer {

    override fun complete(
        reader: LineReader,
        parsedLine: ParsedLine,
        candidates: List<Candidate>
    ) {
        val line = parsedLine.line()
        val possible: ArrayList<Candidate> = ArrayList()

        if (line.isEmpty()) {
            commandService.commands.forEach {
                possible.add(Candidate(it.name, it.name, null, null, null, null, true))
            }
        } else {
            val parts = line.split(" ")
            val command = commandService.commands.find { it.name == parts[0] }
            if(command == null) {
                commandService.commands.filter { it.name.startsWith(parts[0]) }.forEach {
                    possible.add(Candidate(it.name, it.name, null, null, null, null, true))
                }
            }

            if(parts.size > 1 && command != null) {
                for (entry in command.syntaxes) {
                    if (parts.size - 1 <= entry.key.size) {
                        val candidateName = "<${entry.key[parts.size - 2].id.lowercase()}>"
                        possible.add(Candidate(candidateName, " ", null, null, null, null, false))
                        possible.add(Candidate("", candidateName, null, null, null, null, false))
                    }
                }
            }
        }

        (candidates as MutableList<Candidate>).addAll(possible)
    }


}